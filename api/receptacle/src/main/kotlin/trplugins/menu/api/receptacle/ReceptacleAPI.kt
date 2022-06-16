package trplugins.menu.api.receptacle

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.*
import trplugins.menu.api.receptacle.vanilla.window.ChestInventory
import trplugins.menu.api.receptacle.vanilla.window.WindowLayout
import trplugins.menu.api.receptacle.vanilla.window.WindowReceptacle
import java.util.*

private val viewingReceptacleMap = HashMap<UUID, Receptacle<*>>()

fun buildReceptacle(title: String, row: Int = 1, builder: ChestInventory.() -> Unit): ChestInventory {
    return ChestInventory(row, title).also(builder)
}

fun buildReceptacle(title: String, type: WindowLayout = WindowLayout.GENERIC_9X1, builder: WindowReceptacle.() -> Unit): WindowReceptacle {
    return WindowReceptacle(type, title).also(builder)
}

fun Player.openReceptacle(title: String, row: Int = 1, builder: ChestInventory.() -> Unit) {
    buildReceptacle(title, row, builder).open(this)
}

fun Player.openReceptacle(title: String, type: WindowLayout = WindowLayout.GENERIC_9X1, builder: WindowReceptacle.() -> Unit) {
    buildReceptacle(title, type, builder).open(this)
}

fun Player.getViewingReceptacle(): Receptacle<*>? {
    return viewingReceptacleMap[uniqueId]
}

fun Player.setViewingReceptacle(receptacle: Receptacle<*>?) {
    if (receptacle == null) {
        viewingReceptacleMap.remove(uniqueId)
    } else {
        viewingReceptacleMap[uniqueId] = receptacle
    }
}

fun InventoryType.createReceptacle(title: String = defaultTitle): WindowReceptacle {
    if (this != CHEST) {
        val receptacleType = when (this.name) {
            "ENDER_CHEST", "BARREL" -> WindowLayout.GENERIC_9X3
            "DISPENSER", "DROPPER" -> WindowLayout.GENERIC_3x3
            "ANVIL" -> WindowLayout.ANVIL
            "FURNACE" -> WindowLayout.FURNACE
            "WORKBENCH", "CRAFTING" -> WindowLayout.CRAFTING
            "ENCHANTING" -> WindowLayout.ENCHANTMENT
            "BREWING" -> WindowLayout.BREWING_STAND
            "MERCHANT" -> WindowLayout.MERCHANT
            "BEACON" -> WindowLayout.BEACON
            "HOPPER" -> WindowLayout.HOPPER
            "SHULKER_BOX" -> WindowLayout.SHULKER_BOX
            "BLAST_FURNACE" -> WindowLayout.BLAST_FURNACE
            "SMOKER" -> WindowLayout.SMOKER
            "LOOM" -> WindowLayout.LOOM
            "CARTOGRAPHY" -> WindowLayout.CARTOGRAPHY_TABLE
            "GRINDSTONE" -> WindowLayout.GRINDSTONE
            "STONECUTTER" -> WindowLayout.STONECUTTER
            else -> throw IllegalArgumentException("Unsupported $this")
        }
        return WindowReceptacle(receptacleType, title)
    }
    return ChestInventory()
}