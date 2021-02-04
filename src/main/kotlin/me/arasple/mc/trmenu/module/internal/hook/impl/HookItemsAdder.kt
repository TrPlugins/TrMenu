package me.arasple.mc.trmenu.module.internal.hook.impl

import dev.lone.itemsadder.api.CustomStack
import io.izzel.taboolib.internal.xseries.XMaterial
import io.izzel.taboolib.util.item.ItemBuilder
import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/27 11:58
 */
class HookItemsAdder : HookAbstract() {

    private val empty = ItemBuilder(XMaterial.BEDROCK).name("UNHOOKED_${name.toUpperCase()}").build()

    fun checkHooked(): Boolean {
        return if (isHooked) true else false.also { reportAbuse() }
    }

    fun getItem(id: String): ItemStack {
        if (checkHooked()) {
            return CustomStack.getInstance(id)?.itemStack ?: empty
        }
        return empty
    }

    fun getId(itemStack: ItemStack): String {
        if (checkHooked()) {
            return CustomStack.byItemStack(itemStack)?.id ?: "UNKNOWN"
        }
        return "UNHOOKED"
    }

}