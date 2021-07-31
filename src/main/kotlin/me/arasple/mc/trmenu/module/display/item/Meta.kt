package me.arasple.mc.trmenu.module.display.item

import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.script.Condition
import me.arasple.mc.trmenu.util.Regexs
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.module.nms.ItemTag
import taboolib.platform.util.ItemBuilder

/**
 * @author Arasple
 * @date 2021/1/24 18:50
 * 显示物品的非动画, 支持动态的属性
 */
class Meta(
    private val amount: String,
    private val shiny: String,
    private val flags: Array<ItemFlag>,
    private val nbt: ItemTag?,
) {

    private val isAmountDynamic = amount.toIntOrNull() == null
    private val isShinyDynamic = !shiny.matches(Regexs.BOOLEAN)
    private val isNBTDynamic = nbt != null && Regexs.containsPlaceholder(nbt.toJsonSimplified())
    val isDynamic = isAmountDynamic || isNBTDynamic || isShinyDynamic

    fun amount(session: MenuSession): Int {
        return (if (isAmountDynamic) session.parse(amount) else amount).toDoubleOrNull()?.toInt() ?: 1
    }

    fun shiny(session: MenuSession, builder: ItemBuilder) {
        if ((shiny.toBoolean()) || (isShinyDynamic && Condition.eval(session.placeholderPlayer, shiny).asBoolean())) {
            builder.shiny()
        }
        return
    }

    fun flags(builder: ItemBuilder) {
        if (flags.isNotEmpty()) {
            builder.flags.addAll(flags)
        }
    }

    fun nbt(session: MenuSession, itemStack: ItemStack): ItemMeta? {
        if (nbt != null && !nbt.isEmpty()) {
            val nbt = if (isNBTDynamic) ItemTag.fromJson(session.parse(nbt.toJson())) else nbt
            if (nbt.isEmpty()) return itemStack.also { nbt.saveTo(it) }.itemMeta
        }
        return null
    }

    fun hasAmount(): Boolean {
        return amount.isNotEmpty() || amount.toIntOrNull() != null
    }

}