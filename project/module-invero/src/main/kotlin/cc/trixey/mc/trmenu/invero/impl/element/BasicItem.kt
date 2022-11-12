package cc.trixey.mc.trmenu.invero.impl.element

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute
import cc.trixey.mc.trmenu.invero.module.element.ItemProvider
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/11/4 18:37
 */
class BasicItem(private var itemStack: ItemStack?, panel: Panel) : ElementAbsolute(panel), ItemProvider {

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