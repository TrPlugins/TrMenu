package trplugins.menu.util.bukkit

import trplugins.menu.module.internal.service.Performance
import trplugins.menu.util.bukkit.ItemMatcher.TraitType.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.platform.util.hasItem
import taboolib.platform.util.takeItem

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
                raw.split("\\s*;\\s*".toRegex()).mapNotNull {
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
        return matcher.all {
            it.itemsMatcher(itemStack) && if (ignoreAmount) true
            else if (it.amount.first) itemStack.amount == it.amount.second else itemStack.amount != it.amount.second
        }
    }

    fun hasItem(player: Player): Boolean {
        Performance.check("Function:ItemMatcherCheck") {
            return matcher.all { match ->
                if (match.amount.first) player.inventory.any { it?.amount != match.amount.second }
                else player.inventory.hasItem(match.amount.second, match.itemsMatcher)
            }
        }
        throw Exception()
    }

    fun takeItem(player: Player) = matcher.all { match ->
        if (match.amount.first)
            player.inventory.all {
                if (it?.amount != match.amount.second) {
                    player.inventory.remove(it)
                    return@all true
                }
                false
            }
        else
            player.inventory.takeItem(match.amount.second, match.itemsMatcher)
    }

    fun buildItem(): List<ItemStack> {
        return matcher.map {
            taboolib.platform.util.buildItem(XMaterial.BEDROCK) {
                amount = it.amount.second

                it.traits.forEach { (trait, value) ->
                    when (trait.first) {
                        MATERIAL -> setMaterial(XMaterial.valueOf(value.uppercase()))
                        DATA -> damage = (value.toIntOrNull() ?: 0)
                        MODEL_DATA -> customModelData = value.toIntOrNull() ?: 0
                        NAME -> name = value
                        LORE -> lore.addAll(value.split("\n"))
                        HEAD -> skullOwner = value
                        AMOUNT -> amount = value.toIntOrNull() ?: 1
                    }
                }
            }
        }
    }

    class Match(val traits: Map<Pair<TraitType, Boolean>, String>) {

        private fun getTrait(type: TraitType): Pair<Boolean, String>? {
            traits.forEach {
                if (it.key.first == type) {
                    return Pair(it.key.second, it.value)
                }
            }
            return null
        }

        val opposition: (Boolean, Boolean) -> Boolean = { oppose, origin ->
            if (oppose) !origin else origin
        }

        // 是否反判
        private fun <T> oppose(trait: Pair<Boolean, T?>?) =
            trait?.first ?: false

        val amount = getTrait(AMOUNT).let { Pair(oppose(it), it?.second?.toIntOrNull() ?: 1) }

        val itemsMatcher: ((itemStack: ItemStack) -> Boolean) = { itemStack ->
            val material = getTrait(MATERIAL)
            val materialMatch = opposition(oppose(material), material == null || itemStack.type.name.equals(material.second, true))

            val damage = getTrait(DATA)?.let { Pair(it.first, it.second.toShortOrNull()) }
            @Suppress("DEPRECATION")
            val damageMatch = opposition(oppose(damage), damage == null || itemStack.durability == damage.second)

            val modelData = getTrait(MODEL_DATA)?.let { Pair(it.first, it.second.toIntOrNull()) }
            val modelDataMatch = opposition(oppose(modelData), modelData == null || itemStack.itemMeta?.customModelData == modelData.second)

            val name = getTrait(NAME)
            val nameMatch = opposition(oppose(name), name == null || itemStack.itemMeta?.displayName?.contains(name.second.colored(), true) == true)

            val lore = getTrait(LORE)
            val loreMatch = opposition(oppose(lore), lore == null || itemStack.itemMeta?.lore?.any { it.contains(lore.second.colored(), true) } ?: false)

            val head = getTrait(HEAD)
            val headMatch = head == null || kotlin.run {
                val itemMeta = itemStack.itemMeta
                if (itemMeta is SkullMeta) {
                    val ownerPlayer = itemMeta.owningPlayer?.name
                    val texture = Heads.seekTexture(itemStack)
                    opposition(oppose(head), head.second.equals(ownerPlayer, true) || head.second.equals(texture, true))
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

        constructor(regex: String) : this("(?i)$regex".toRegex())

        companion object {

            // Trait, Opposite
            fun of(type: String): Pair<TraitType, Boolean>? {
                val trait = values().find { it.regex.matches(type.removePrefix("!")) } ?: return null
                val oppose = type.first() == '!'
                return Pair(trait, oppose)
            }

        }

    }

}