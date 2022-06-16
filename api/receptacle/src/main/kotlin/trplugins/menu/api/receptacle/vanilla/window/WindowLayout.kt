package trplugins.menu.api.receptacle.vanilla.window

import org.bukkit.event.inventory.InventoryType
import trplugins.menu.api.receptacle.ReceptacleLayout

/**
 * TrMenu
 * trplugins.menu.api.receptacle.ReceptacleVanillaLayout
 *
 * @author Score2
 * @since 2022/03/05 14:35
 */
open class WindowLayout(val vanillaId: String, val serialId: Int, slotRange: IntRange) : ReceptacleLayout(slotRange) {

    val id by lazy { "minecraft:${toBukkitType().name.lowercase()}" }
    fun toBukkitType(): InventoryType {
        return when (serialId) {
            0, 1, 2, 3, 4, 5 -> InventoryType.CHEST
            6 -> InventoryType.HOPPER
            7 -> InventoryType.ANVIL
            8 -> InventoryType.BEACON
            9 -> InventoryType.BLAST_FURNACE
            10 -> InventoryType.BREWING
            11 -> InventoryType.CRAFTING
            12 -> InventoryType.ENCHANTING
            13 -> InventoryType.FURNACE
            14 -> InventoryType.GRINDSTONE
            15 -> InventoryType.HOPPER
            16 -> InventoryType.LOOM
            17 -> InventoryType.MERCHANT
            18 -> InventoryType.SHULKER_BOX
            19 -> InventoryType.SMOKER
            20 -> InventoryType.CARTOGRAPHY
            21 -> InventoryType.STONECUTTER
            else -> InventoryType.CHEST
        }
    }

    companion object {

        /**
         * A 1-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X1 = WindowLayout(
            "GENERIC_9x1",
            0,
            0..8
        )

        /**
         * A 2-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X2 = WindowLayout(
            "GENERIC_9x2",
            1,
            0..17
        )

        /**
         * General-purpose 3-row inventory. Used by Chest, minecart with chest, ender chest, and barrel
         */
        @JvmField
        val GENERIC_9X3 = WindowLayout(
            "GENERIC_9x3",
            2,
            0..26
        )

        /**
         * A 4-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X4 = WindowLayout(
            "GENERIC_9x4",
            3,
            0..35
        )

        /**
         * A 5-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X5 = WindowLayout(
            "GENERIC_9x5",
            4,
            0..44
        )

        /**
         * General-purpose 6-row inventory, used by large chests.
         */
        @JvmField
        val GENERIC_9X6 = WindowLayout(
            "GENERIC_9x6",
            5,
            0..53
        )

        /**
         * General-purpose 3-by-3 square inventory, used by Dispenser and Dropper
         */
        @JvmField
        val GENERIC_3x3 = WindowLayout(
            "GENERIC_3x3",
            6,
            0..8
        )

        /**
         * Anvil
         */
        @JvmField
        val ANVIL = WindowLayout(
            "ANVIL",
            7,
            0..2
        )

        /**
         * Beacon
         */
        @JvmField
        val BEACON = WindowLayout(
            "BEACON",
            8,
            0..0
        )

        @JvmField
        val BLAST_FURNACE = WindowLayout(
            "BLAST_FURNACE",
            9,
            0..2
        )

        @JvmField
        val BREWING_STAND = WindowLayout(
            "BREWING_STAND",
            10,
            0..4
        )

        @JvmField
        val CRAFTING = WindowLayout(
            "CRAFTING",
            11,
            0..9
        )

        @JvmField
        val ENCHANTMENT = WindowLayout(
            "ENCHANTMENT",
            12,
            0..1
        )

        @JvmField
        val FURNACE = WindowLayout(
            "FURNACE",
            13,
            0..2
        )


        @JvmField
        val GRINDSTONE = WindowLayout(
            "GRINDSTONE",
            14,
            0..2
        )

        @JvmField
        val HOPPER = WindowLayout(
            "HOPPER",
            15,
            0..4
        )

        @JvmField
        val LOOM = WindowLayout(
            "LOOM",
            17,
            0..3
        )

        @JvmField
        val MERCHANT = WindowLayout(
            "MERCHANT",
            18,
            0..2
        )

        @JvmField
        val SHULKER_BOX = WindowLayout(
            "SHULKER_BOX",
            19,
            0..26
        )

        @JvmField
        val SMOKER = WindowLayout(
            "SMOKER",
            20,
            0..2
        )

        @JvmField
        val CARTOGRAPHY_TABLE = WindowLayout(
            "CARTOGRAPHY_TABLE",
            21,
            0..2
        )

        @JvmField
        val STONECUTTER = WindowLayout(
            "STONECUTTER",
            22,
            0..1
        )

        @JvmStatic
        fun ofRows(rows: Int): WindowLayout {
            return when (rows) {
                1 -> GENERIC_9X1
                2 -> GENERIC_9X2
                3 -> GENERIC_9X3
                4 -> GENERIC_9X4
                5 -> GENERIC_9X5
                6 -> GENERIC_9X6
                else -> throw IllegalArgumentException("Rows for chest must be an integer between [1, 6]")
            }
        }
    }

}