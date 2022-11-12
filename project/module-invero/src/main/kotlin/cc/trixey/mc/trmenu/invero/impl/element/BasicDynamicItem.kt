package cc.trixey.mc.trmenu.invero.impl.element

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.element.ElementDynamic
import cc.trixey.mc.trmenu.invero.module.element.ItemProvider
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/11/12 19:52
 */
class BasicDynamicItem(private var itemStack: ItemStack?, panel: Panel) : ElementDynamic(panel), ItemProvider {

    override fun renderElement(window: Window) {
        if (!panel.isRenderable(this)) return

        window.getAbsoluteSlots().forEach {
            window.pairedInventory[it] = itemStack
        }
    }

    override fun get(): ItemStack? {
        return itemStack
    }

    override fun setItem(itemStack: ItemStack?) {
        this.itemStack = itemStack
    }


}