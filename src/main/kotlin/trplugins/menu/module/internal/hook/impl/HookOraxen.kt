package trplugins.menu.module.internal.hook.impl

import io.th0rgal.oraxen.items.OraxenItems
import trplugins.menu.module.internal.hook.HookAbstract
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem

/**
 * @author Arasple
 * @date 2021/1/27 11:58
 */
class HookOraxen : HookAbstract() {

    private val empty = buildItem(XMaterial.BEDROCK) { name = "UNHOOKED_${super.name.uppercase()}" }

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