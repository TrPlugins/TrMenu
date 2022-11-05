package cc.trixey.mc.trmenu.invero.impl.element

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.PanelElement
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/11/4 18:37
 */
class BaseItem(private var itemStack: ItemStack?, override val panel: Panel) : PanelElement {

    fun setItem(itemStack: ItemStack?) {
        this.itemStack = itemStack
    }

    override fun renderItem(): ItemStack? {
        return itemStack
    }

}