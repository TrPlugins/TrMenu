package me.arasple.mc.trmenu.display.item

import io.izzel.taboolib.Version
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Materials
import me.arasple.mc.trmenu.configuration.property.Nodes
import me.arasple.mc.trmenu.configuration.property.Nodes.*
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.modules.hook.HookHeadDatabase
import me.arasple.mc.trmenu.modules.hook.HookSkinsRestorer
import me.arasple.mc.trmenu.modules.repo.ItemRepository
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Skulls
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*
import kotlin.math.min

/**
 * @author Arasple
 * @date 2020/5/30 14:08
 */
class DynamicItem(var material: Animated<Mat>, val meta: Meta) {

    fun getItem(player: Player) = material.currentElement(player)

    fun nextItem(player: Player) = material.nextIndex(player)

    fun releaseItem(player: Player, name: String?, lore: List<String>?): ItemStack? {
        val itemStack = getItem(player)?.createItem(player)?.clone() ?: return null
        val itemMeta = itemStack.itemMeta

        if (meta.hasAmount()) {
            itemStack.amount = meta.amount(player)
        }
        if (itemMeta != null) {
            meta.shiny(player, itemMeta)
            meta.flags.forEach { itemMeta.addItemFlags(it) }
            name?.let { itemMeta.setDisplayName(it) }
            lore?.let { itemMeta.lore = it }
        }
        itemStack.itemMeta = itemMeta
        meta.nbt(player, itemStack)?.also { itemStack.itemMeta = it }
        return itemStack
    }

    class Mat(val raw: String, val value: String, val type: Pair<Nodes, String>, val dynamic: Boolean) {

        var staticItemBuilder: ItemBuilder? = null
        var staticItem: ItemStack? = null

        fun createItem(player: Player): ItemStack {
            val value = if (dynamic) Msger.replace(player, value) else value
            val typeValue = if (dynamic) Msger.replace(player, type.second) else type.second

            return when (type.first) {
                MAT_REPOSITORY -> ItemRepository.getItem(typeValue) ?: ItemStack(Material.BARRIER)
                MAT_HEAD -> Skulls.getPlayerHead(typeValue)
                MAT_TEXTURED_SKULL -> Skulls.getTextureSkull(typeValue)
                MAT_HEAD_DATABASE -> HookHeadDatabase.getHead(typeValue)
                MAT_HEAD_SKINSRESTORER -> Skulls.getTextureSkull(HookSkinsRestorer.getSkin(typeValue))
                MAT_SCRIPT -> Scripts.script(player, type.second, true).asItemStack()!!
                MAT_JSON -> Items.fromJson(typeValue)
                else -> {
                    if (staticItem != null) {
                        return staticItem!!
                    }
                    val builder = if (dynamic) serializeItemBuilder(value) else staticItemBuilder.let {
                        if (it == null) staticItemBuilder = serializeItemBuilder(value)
                        return@let staticItemBuilder!!
                    }
                    when (type.first) {
                        MAT_DATA_VALUE -> builder.damage(NumberUtils.toInt(typeValue, 0))
                        MAT_DYE_LEATHER -> builder.color(serializeColor(typeValue))
                        else -> {
                            // TODO BANNER
                        }
                    }
                    val itemStack = builder.build()
                    if (!dynamic) staticItem = itemStack
                    if (type.first == MAT_MODEL_DATA && Version.isAfter(Version.v1_14)) {
                        itemStack.itemMeta.let { meta ->
                            meta?.setCustomModelData(NumberUtils.toInt(typeValue, 0)).also { itemStack.itemMeta = meta }
                        }
                    }
                    return itemStack
                }
            }
        }
    }

    class Meta(var amount: String, var shiny: String, var flags: MutableSet<ItemFlag>, var nbt: NBTCompound?, var isAmountDynamic: Boolean, var isShinyDynamic: Boolean, var isNBTDynamic: Boolean) {

        constructor() : this("-1", "false", mutableSetOf(), null, false, false, false)

        fun amount(string: String) {
            this.amount = string
            this.isAmountDynamic = !NumberUtils.isParsable(amount)
        }

        fun shiny(string: String) {
            this.shiny = string
            this.isShinyDynamic = !shiny.matches(Regex("(?i)true|false"))
        }

        fun flags(flags: MutableList<String>) {
            this.flags.clear()
            flags.forEach {
                val flag = Items.asItemFlag(it)
                if (flag != null) this.flags.add(flag)
            }
        }

        fun nbt(nbt: ConfigurationSection?) {
            if (nbt == null) return
            if (nbt.getValues(false).isEmpty()) {
                this.nbt = NBTCompound.translateSection(NBTCompound(), nbt)
                this.isNBTDynamic = Msger.containsPlaceholders(this.nbt?.toJsonSimplified())
            }
        }

        fun amount(player: Player): Int = NumberUtils.toInt(if (isAmountDynamic) Msger.replace(player, amount) else amount, 1)

        fun shiny(player: Player, itemMeta: ItemMeta) {
            if ((shiny.toBoolean()) || (isShinyDynamic && Scripts.expression(player, shiny).asBoolean())) {
                itemMeta.addEnchant(Enchantment.LUCK, 1, true)
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
            return
        }

        fun nbt(player: Player, itemStack: ItemStack): ItemMeta? {
            if (nbt != null && !nbt?.isEmpty()!!) {
                val nbt = if (isNBTDynamic) NBTCompound.fromJson(nbt?.toJson()?.let { Msger.replace(player, it) }) else nbt
                if (!nbt?.isEmpty()!!) return NMS.handle().saveNBT(itemStack, nbt).itemMeta
            }
            return null
        }

        fun hasAmount(): Boolean = amount.isNotEmpty() && NumberUtils.toInt(amount, -1) >= 0

    }

    companion object {

        fun serializeColor(color: String): Color {
            val rgb = color.split(",").toTypedArray()
            if (rgb.size == 3) {
                val r = min(NumberUtils.toInt(rgb[0], 0), 255)
                val g = min(NumberUtils.toInt(rgb[1], 0), 255)
                val b = min(NumberUtils.toInt(rgb[2], 0), 255)
                return Color.fromRGB(r, g, b)
            }
            return Color.BLACK
        }

        fun serializeItemBuilder(value: String): ItemBuilder {
            val split = value.split(":")
            var builder = ItemBuilder(Material.BARRIER)
            val data = if (split.size >= 2) NumberUtils.toInt(split[1], 0) else 0
            try {
                if (NumberUtils.isParsable(split[0])) {
                    val id = NumberUtils.toInt(split[0], -1)
                    if (id >= 0) builder.material(id)
                    if (data > 0) builder.damage(data)
                } else {
                    val name = split[0].toUpperCase(Locale.ENGLISH).replace(Regex("( )|-"), "_")
                    val materials = Materials.values().maxBy { Strings.similarDegree(name, it.name) }
                    if (materials != null) {
                        val pared = materials.parseItem()
                        builder = if (data > 0) ItemBuilder(pared).damage(data)
                        else ItemBuilder(pared)
                    } else {
                        builder.material(Material.valueOf(split[0]))
                    }
                }
            } catch (e: Throwable) {
                Msger.printErrors("MATERIAL", value)
            }
            return builder
        }

        fun createMat(raw: String): Mat {
            val result = Nodes.read(raw)
            val left = result.first
            val nodes = result.second.entries.firstOrNull()
            var type = nodes?.key
            var typeValue = nodes?.value
            if (Utils.isJson(left)) {
                type = MAT_JSON
                typeValue = left
            }
            val types = if (type != null) Pair(type, typeValue ?: "") else Pair(MAT_ORIGINAL, result.first)
            return Mat(raw, result.first, types, Msger.containsPlaceholders(raw) || type == MAT_VARIABLE)
        }

    }

}