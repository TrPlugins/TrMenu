package me.arasple.mc.trmenu.util.bukkit

import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.module.internal.service.Performance
import me.arasple.mc.trmenu.util.bukkit.ItemMatcher.TraitType.*
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

/**
 * @author Arasple
 * @date 2021/1/25 15:14
 */
class ItemMatcher(private val matcher: Set<Match>) {

    companion object {

        private val cachedItemMatchers = mutableMapOf<String, ItemMatcher>()

        fun eval(str: String, cache: Boolean = true): ItemMatcher {
            return if (cache) cachedItemMatchers.computeIfAbsent(str) { of(str) } else of(str)
        }

        fun of(raw: String): ItemMatcher {
            val matchers =
                raw.split(";").mapNotNull {
                    val match = it.split(",").mapNotNull { trait ->
                        val traits = trait.split(":", "=", limit = 2)

                        if (traits.size >= 2) {
                            val type = TraitType.of(traits[0])
                            if (type != null) type to traits[1]
                            else null
                        } else {
                            null
                        }
                    }.toMap()

                    if (match.isNotEmpty()) Match(match)
                    else null
                }

            return ItemMatcher(matchers.toSet())
        }

    }

    fun itemMatches(itemStack: ItemStack, ignoreAmount: Boolean = false): Boolean {
        return matcher.all { it.itemsMatcher.match(itemStack) && if (ignoreAmount) true else itemStack.amount == it.amount }
    }

    fun hasItem(player: Player): Boolean {
        Performance.MIRROR.check("Function:ItemMatcherCheck") {
            return matcher.all {
                Items.hasItem(player.inventory, it.itemsMatcher, it.amount)
            }
        }
        throw Exception()
    }

    fun takeItem(player: Player): Boolean {
        return matcher.all {
            Items.takeItem(player.inventory, it.itemsMatcher, it.amount)
        }
    }

    fun buildItem(): List<ItemStack> {
        return matcher.map {
            val itemBuilder = ItemBuilder(Material.BEDROCK).amount(it.amount)

            it.traits.forEach { (trait, value) ->
                when (trait) {
                    MATERIAL -> itemBuilder.material(value.toUpperCase())
                    DATA -> itemBuilder.damage(value.toIntOrNull() ?: 0)
                    MODEL_DATA -> itemBuilder.customModelData(value.toIntOrNull() ?: 0)
                    NAME -> itemBuilder.name(value)
                    LORE -> itemBuilder.lore(value.split("\n"))
                    HEAD -> itemBuilder.skullOwner(value)
                    else -> {
                    }
                }
            }

            itemBuilder.colored().build()
        }
    }

    class Match(val traits: Map<TraitType, String>) {

        private fun getTrait(type: TraitType): String? {
            return traits[type]
        }

        val amount = getTrait(AMOUNT)?.toIntOrNull() ?: 1

        val itemsMatcher = Items.Matcher { itemStack ->
            val material = getTrait(MATERIAL)
            val materialMatch = material == null || itemStack.type.name.equals(material, true)

            val damage = getTrait(DATA)?.toShortOrNull()

            @Suppress("DEPRECATION")
            val damageMatch = damage == null || itemStack.durability == damage

            val modelData = getTrait(MODEL_DATA)?.toIntOrNull()
            val modelDataMatch = modelData == null || itemStack.itemMeta?.customModelData == modelData

            val name = getTrait(NAME)
            val nameMatch = name == null || itemStack.itemMeta?.displayName?.contains(name, true) == true

            val lore = getTrait(LORE)
            val loreMatch = lore == null || itemStack.itemMeta?.lore?.any { it.contains(lore, true) } ?: false

            val head = getTrait(HEAD)
            val headMatch = head == null || kotlin.run {
                val itemMeta = itemStack.itemMeta
                if (itemMeta is SkullMeta) {
                    val ownerPlayer = itemMeta.owningPlayer?.name
                    val texture = Heads.seekTexture(itemStack)
                    head.equals(ownerPlayer, true) || head.equals(texture, true)
                }
                false
            }

            materialMatch && damageMatch && nameMatch && modelDataMatch && loreMatch && headMatch
        }

    }

    enum class TraitType(val regex: Regex) {

        MATERIAL("mat(erial)?s?"),

        AMOUNT("(amount|amt)s?"),

        DATA("datas?"),

        MODEL_DATA("model-?datas?"),

        NAME("names?"),

        LORE("lores?"),

        HEAD("(head|skull|texture)s?");

        constructor(regex: String) : this(regex.toRegex())

        companion object {

            fun of(type: String): TraitType? {
                return values().find { it.regex.matches(type) }
            }

        }

    }

}