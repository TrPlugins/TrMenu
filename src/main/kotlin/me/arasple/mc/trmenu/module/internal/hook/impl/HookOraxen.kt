package me.arasple.mc.trmenu.module.internal.hook.impl

import io.th0rgal.oraxen.items.OraxenItems
import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.ItemBuilder
import java.util.*

/**
 * @author Arasple
 * @date 2021/1/27 11:58
 */
class HookOraxen : HookAbstract() {

    private val empty = ItemBuilder(XMaterial.BEDROCK).also { it.name = "UNHOOKED_${name.uppercase()}" }.build()

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