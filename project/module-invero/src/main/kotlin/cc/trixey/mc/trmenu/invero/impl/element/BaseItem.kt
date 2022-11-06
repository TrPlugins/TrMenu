package cc.trixey.mc.trmenu.invero.impl.element

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.PanelElementAbsolute
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/11/4 18:37
 */
open class BaseItem(private var itemStack: ItemStack?, panel: Panel) : PanelElementAbsolute(panel) {

    private var handlerClick: (e: InventoryClickEvent) -> Unit = {}

    fun setItem(itemStack: ItemStack?) {
        this.itemStack = itemStack
    }

    fun modifyItem(function: ItemStack.() -> Unit) {
        itemStack?.let { function(it) }
    }

    fun onClick(pass: InventoryClickEvent.() -> Unit) {
        handlerClick = pass
    }

    override fun handleClick(e: InventoryClickEvent) {
        handlerClick(e)
    }

    override fun render() {
        forWindows {
            actualSlots().forEach {
                pairedInventory[it] = itemStack
            }
        }
    }

}