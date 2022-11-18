package cc.trixey.mc.trmenu.invero.impl

import cc.trixey.mc.trmenu.invero.module.PairedInventory
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/10/29 16:42
 */
class WindowHolder(val window: Window) : InventoryHolder {

    companion object {

        val runningWindows = CopyOnWriteArrayList<Window>()

        fun Window.register() {
            panels.forEach { it.registerWindow(this) }
            runningWindows.add(this)
        }

        fun Window.unregister(): Boolean {
            panels.forEach { it.unregisterWindow(this) }
            return runningWindows.remove(this)
        }

    }

    override fun getInventory(): Inventory {
        return (window.inventorySet as PairedInventory).container
    }

}