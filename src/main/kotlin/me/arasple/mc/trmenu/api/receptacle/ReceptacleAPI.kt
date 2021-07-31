package me.arasple.mc.trmenu.api.receptacle

import me.arasple.mc.trmenu.api.receptacle.manager.Manager
import me.arasple.mc.trmenu.api.receptacle.window.Receptacle
import me.arasple.mc.trmenu.api.receptacle.window.ReceptacleType
import me.arasple.mc.trmenu.api.receptacle.window.type.InventoryChest
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.*

/**
 * @author Arasple
 * @date 2021/1/17 11:03
 */
object ReceptacleAPI {

    val MANAGER = Manager()

    fun createReceptacle(inventoryType: InventoryType, title: String = "Def"): Receptacle {
        if (inventoryType == CHEST) return InventoryChest()

        val receptacleType = when (inventoryType) {
            ENDER_CHEST, BARREL -> ReceptacleType.GENERIC_9X3
            DISPENSER, DROPPER -> ReceptacleType.GENERIC_3X3
            ANVIL -> ReceptacleType.ANVIL
            FURNACE -> ReceptacleType.FURNACE
            WORKBENCH, CRAFTING -> ReceptacleType.CRAFTING
            ENCHANTING -> ReceptacleType.ENCHANTMENT_TABLE
            BREWING -> ReceptacleType.BREWING_STAND
            MERCHANT -> ReceptacleType.MERCHANT
            BEACON -> ReceptacleType.BEACON
            HOPPER -> ReceptacleType.HOPPER
            SHULKER_BOX -> ReceptacleType.SHULKER_BOX
            BLAST_FURNACE -> ReceptacleType.BLAST_FURNACE
            SMOKER -> ReceptacleType.SMOKER
            LOOM -> ReceptacleType.LOOM
            CARTOGRAPHY -> ReceptacleType.CARTOGRAPHY
            GRINDSTONE -> ReceptacleType.GRINDSTONE
            STONECUTTER -> ReceptacleType.STONE_CUTTER
            else -> throw IllegalArgumentException("Do not support $inventoryType")
        }

        return Receptacle(inventoryType, receptacleType, title)
    }

}