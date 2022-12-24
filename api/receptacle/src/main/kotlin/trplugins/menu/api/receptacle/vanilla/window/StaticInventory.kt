package trplugins.menu.api.receptacle.vanilla.window

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import taboolib.library.reflex.Reflex.Companion.getProperty

object StaticInventory {

    private val inventories = HashMap<String, Holder>()

    val Player.staticContainerId get() = inventories[this.name]?.containerId
    val Player.staticInventory get() = inventories[this.name]?.inventory

    fun open(player: Player, layout: WindowLayout, title: String) {
        val holder = Holder(layout, title)
        holder.open(player)
        inventories[player.name] = holder
    }

    fun close(player: Player) {
        player.closeInventory()
        inventories.remove(player.name)?.clear()
    }

    class Holder(layout: WindowLayout, title: String) : InventoryHolder {

        private val inventory: Inventory
        var containerId: Int? = null
            private set

        init {
            inventory = when (val type = layout.toBukkitType()) {
                InventoryType.CHEST -> Bukkit.createInventory(this, layout.slotRange.last + 1, title)
                else -> Bukkit.createInventory(this, type, title)
            }
        }

        override fun getInventory(): Inventory {
            return inventory
        }

        fun open(player: Player) {
            player.openInventory(inventory)
            containerId = player.getProperty<Int>("entity/containerCounter")!!
        }

        fun clear() {
            inventory.clear()
            containerId = null
        }
    }
}