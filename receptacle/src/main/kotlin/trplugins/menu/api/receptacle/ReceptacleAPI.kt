package trplugins.menu.api.receptacle

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.*
import java.util.*

private val viewingReceptacleMap = HashMap<UUID, Receptacle>()

fun buildReceptacle(title: String, row: Int = 1, builder: ChestInventory.() -> Unit): ChestInventory {
    return ChestInventory(row, title).also(builder)
}

fun buildReceptacle(title: String, type: VanillaLayout = VanillaLayout.GENERIC_9X1, builder: Receptacle.() -> Unit): Receptacle {
    return Receptacle(type, title).also(builder)
}

fun Player.openReceptacle(title: String, row: Int = 1, builder: ChestInventory.() -> Unit) {
    buildReceptacle(title, row, builder).open(this)
}

fun Player.openReceptacle(title: String, type: VanillaLayout = VanillaLayout.GENERIC_9X1, builder: Receptacle.() -> Unit) {
    buildReceptacle(title, type, builder).open(this)
}

fun Player.getViewingReceptacle(): Receptacle? {
    return viewingReceptacleMap[uniqueId]
}

fun Player.setViewingReceptacle(receptacle: Receptacle?) {
    if (receptacle == null) {
        viewingReceptacleMap.remove(uniqueId)
    } else {
        viewingReceptacleMap[uniqueId] = receptacle
    }
}

fun InventoryType.createReceptacle(title: String = defaultTitle): Receptacle {
    if (this != CHEST) {
        val receptacleType = when (this.name) {
            "ENDER_CHEST", "BARREL" -> VanillaLayout.GENERIC_9X3
            "DISPENSER", "DROPPER" -> VanillaLayout.GENERIC_3X3
            "ANVIL" -> VanillaLayout.ANVIL
            "FURNACE" -> VanillaLayout.FURNACE
            "WORKBENCH", "CRAFTING" -> VanillaLayout.CRAFTING
            "ENCHANTING" -> VanillaLayout.ENCHANTMENT_TABLE
            "BREWING" -> VanillaLayout.BREWING_STAND
            "MERCHANT" -> VanillaLayout.MERCHANT
            "BEACON" -> VanillaLayout.BEACON
            "HOPPER" -> VanillaLayout.HOPPER
            "SHULKER_BOX" -> VanillaLayout.SHULKER_BOX
            "BLAST_FURNACE" -> VanillaLayout.BLAST_FURNACE
            "SMOKER" -> VanillaLayout.SMOKER
            "LOOM" -> VanillaLayout.LOOM
            "CARTOGRAPHY" -> VanillaLayout.CARTOGRAPHY
            "GRINDSTONE" -> VanillaLayout.GRINDSTONE
            "STONECUTTER" -> VanillaLayout.STONE_CUTTER
            else -> throw IllegalArgumentException("Unsupported $this")
        }
        return Receptacle(receptacleType, title)
    }
    return ChestInventory()
}