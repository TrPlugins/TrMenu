package me.arasple.mc.trmenu.display.item.property

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * @author Arasple
 * @date 2020/7/27 17:00
 */
data class Meta(var amount: String, var shiny: String, var flags: MutableSet<ItemFlag>, var nbt: NBTCompound?, var isAmountDynamic: Boolean, var isShinyDynamic: Boolean, var isNBTDynamic: Boolean) {

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

    fun amount(player: Player): Int {
        return NumberUtils.toDouble(if (isAmountDynamic) Msger.replace(player, amount) else amount, 1.0).toInt()
    }

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

    fun hasAmount(): Boolean {
        return amount.isNotEmpty() || NumberUtils.toInt(amount, -1) >= 0
    }

}