package me.arasple.mc.trmenu.module.display.item

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import io.izzel.taboolib.util.item.ItemBuilder
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.script.Condition
import me.arasple.mc.trmenu.util.Regexs
import me.arasple.mc.trmenu.util.Utils
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * @author Arasple
 * @date 2021/1/24 18:50
 * 显示物品的非动画, 支持动态的属性
 */
class Meta(
    private val amount: String,
    private val shiny: String,
    private val flags: Array<ItemFlag>,
    private val nbt: NBTCompound?,
) {

    private val isAmountDynamic = amount.toIntOrNull() == null
    private val isShinyDynamic = !shiny.matches(Regexs.BOOLEAN)
    private val isNBTDynamic = nbt != null && Utils.containsPlaceholder(nbt.toJsonSimplified())

    fun amount(session: MenuSession): Int {
        return NumberUtils.toDouble(if (isAmountDynamic) session.parse(amount) else amount, 1.0).toInt()
    }

    fun shiny(session: MenuSession, builder: ItemBuilder) {
        if ((shiny.toBoolean()) || (isShinyDynamic && Condition.eval(session.placeholderPlayer, shiny).asBoolean())) {
            builder.shiny()
        }
        return
    }

    fun flags(builder: ItemBuilder) {
        if (flags.isNotEmpty()) {
            builder.flags(*flags)
        }
    }

    fun nbt(session: MenuSession, itemStack: ItemStack): ItemMeta? {
        if (nbt != null && !nbt.isEmpty()) {
            val nbt = if (isNBTDynamic) NBTCompound.fromJson(session.parse(nbt.toJson())) else nbt
            if (nbt.isEmpty()) return NMS.handle().saveNBT(itemStack, nbt).itemMeta
        }
        return null
    }

    fun hasAmount(): Boolean {
        return amount.isNotEmpty() || NumberUtils.toInt(amount, -1) >= 0
    }

}