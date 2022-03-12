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
open class WindowLayout(val vanillaId: Int, slotRange: IntRange): ReceptacleLayout(slotRange) {

    val id by lazy { "minecraft:${toBukkitType().name.lowercase()}" }

    fun toBukkitType(): InventoryType {
        return when (vanillaId) {
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
            0,
            0..8
        )

        /**
         * A 2-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X2 = WindowLayout(
            1,
            0..17
        )

        /**
         * General-purpose 3-row inventory. Used by Chest, minecart with chest, ender chest, and barrel
         */
        @JvmField
        val GENERIC_9X3 = WindowLayout(
            2,
            0..26
        )

        /**
         * A 4-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X4 = WindowLayout(
            3,
            0..35
        )

        /**
         * A 5-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X5 = WindowLayout(
            4,
            0..44
        )

        /**
         * General-purpose 6-row inventory, used by large chests.
         */
        @JvmField
        val GENERIC_9X6 = WindowLayout(
            5,
            0..53
        )

        /**
         * General-purpose 3-by-3 square inventory, used by Dispenser and Dropper
         */
        @JvmField
        val GENERIC_3X3 = WindowLayout(
            6,
            0..8
        )

        /**
         * Anvil
         */
        @JvmField
        val ANVIL = WindowLayout(
            7,
            0..2
        )

        /**
         * Beacon
         */
        @JvmField
        val BEACON = WindowLayout(
            8,
            0..0
        )

        @JvmField
        val BLAST_FURNACE = WindowLayout(
            9,
            0..2
        )

        @JvmField
        val BREWING_STAND = WindowLayout(
            10,
            0..4
        )

        @JvmField
        val CRAFTING = WindowLayout(
            11,
            0..9
        )

        @JvmField
        val ENCHANTMENT_TABLE = WindowLayout(
            12,
            0..1
        )

        @JvmField
        val FURNACE = WindowLayout(
            13,
            0..2
        )


        @JvmField
        val GRINDSTONE = WindowLayout(
            14,
            0..2
        )

        @JvmField
        val HOPPER = WindowLayout(
            15,
            0..4
        )

        @JvmField
        val LOOM = WindowLayout(
            17,
            0..3
        )

        @JvmField
        val MERCHANT = WindowLayout(
            18,
            0..2
        )

        @JvmField
        val SHULKER_BOX = WindowLayout(
            19,
            0..26
        )

        @JvmField
        val SMOKER = WindowLayout(
            20,
            0..2
        )

        @JvmField
        val CARTOGRAPHY = WindowLayout(
            21,
            0..2
        )

        @JvmField
        val STONE_CUTTER = WindowLayout(
            22,
            0..1
        )


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