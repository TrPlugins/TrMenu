package cc.trixey.mc.trmenu.legacy.invero.bukkit

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * @author Arasple
 * @since 2022/10/23
 */
class BukkitInveroHolder(val invero: cc.trixey.mc.trmenu.legacy.invero.Invero) : InventoryHolder {

    private val inventory: Inventory =
        invero.property.let {
            if (it.isOrdinaryChest) {
                Bukkit.createInventory(this@BukkitInveroHolder, it.rows * 9, invero.title)
            } else {
                Bukkit.createInventory(this@BukkitInveroHolder, InventoryType.valueOf(it.bukkitType), invero.title)
            }
        }

    override fun getInventory(): Inventory {
        return inventory
    }

    companion object {

        fun fromInventory(inventory: Inventory): cc.trixey.mc.trmenu.legacy.invero.Invero? {
            return (inventory.holder as? BukkitInveroHolder)?.invero
        }
    }

}