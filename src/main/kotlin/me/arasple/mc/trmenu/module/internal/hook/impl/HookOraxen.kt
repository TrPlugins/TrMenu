package me.arasple.mc.trmenu.module.internal.hook.impl

import io.izzel.taboolib.internal.xseries.XMaterial
import io.izzel.taboolib.util.item.ItemBuilder
import io.th0rgal.oraxen.items.OraxenItems
import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/27 11:58
 */
class HookOraxen : HookAbstract() {

    private val empty = ItemBuilder(XMaterial.BEDROCK).name("UNHOOKED_${name.toUpperCase()}").build()

    fun checkHooked(): Boolean {
        return if (isHooked) true else false.also { reportAbuse() }
    }

    fun getItem(id: String): ItemStack {
        if (checkHooked()) {
            return OraxenItems.getItemById(id).build()
        }
        return empty
    }

    fun getId(itemStack: ItemStack): String {
        if (checkHooked()) {
            return OraxenItems.getIdByItem(itemStack)
        }
        return "UNHOOKED"
    }

}