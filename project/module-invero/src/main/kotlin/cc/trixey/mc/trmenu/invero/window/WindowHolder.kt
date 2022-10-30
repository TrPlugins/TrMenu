package cc.trixey.mc.trmenu.invero.window

import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * @author Arasple
 * @since 2022/10/29 16:42
 */
class WindowHolder(val window: Window) : InventoryHolder {

    override fun getInventory(): Inventory {
        return window.pairedInventory.container
    }

}