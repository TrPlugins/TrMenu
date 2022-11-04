package cc.trixey.mc.trmenu.invero.impl

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

        fun registerWindow(window: Window) {
            runningWindows.add(window)
        }

        fun unregisterWindow(window: Window): Boolean {
            return runningWindows.remove(window)
        }

    }

    override fun getInventory(): Inventory {
        return window.pairedInventory.container
    }

}