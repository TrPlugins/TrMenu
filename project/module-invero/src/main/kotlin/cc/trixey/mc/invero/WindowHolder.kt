package cc.trixey.mc.invero

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * @author Arasple
 * @since 2022/10/29 16:42
 *
 * BukkitWindow 需要使用的 InventoryHolder
 */
class WindowHolder(val window: Window) : InventoryHolder {

    override fun getInventory(): Inventory {
        return (window.inventorySet as WindowInventory).container
    }

}