package trplugins.menu.api.receptacle

import org.bukkit.event.inventory.InventoryType

/**
 * TrMenu
 * trplugins.menu.api.receptacle.ReceptacleVanillaLayout
 *
 * @author Score2
 * @since 2022/03/05 14:35
 */
open class VanillaLayout(val vanillaId: Int, slotRange: IntRange): ReceptacleLayout(slotRange) {

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
        val GENERIC_9X1 = VanillaLayout(
            0,
            0..8
        )

        /**
         * A 2-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X2 = VanillaLayout(
            1,
            0..17
        )

        /**
         * General-purpose 3-row inventory. Used by Chest, minecart with chest, ender chest, and barrel
         */
        @JvmField
        val GENERIC_9X3 = VanillaLayout(
            2,
            0..26
        )

        /**
         * A 4-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X4 = VanillaLayout(
            3,
            0..35
        )

        /**
         * A 5-row inventory, not used by the notchian server.
         */
        @JvmField
        val GENERIC_9X5 = VanillaLayout(
            4,
            0..44
        )

        /**
         * General-purpose 6-row inventory, used by large chests.
         */
        @JvmField
        val GENERIC_9X6 = VanillaLayout(
            5,
            0..53
        )

        /**
         * General-purpose 3-by-3 square inventory, used by Dispenser and Dropper
         */
        @JvmField
        val GENERIC_3X3 = VanillaLayout(
            6,
            0..8
        )

        /**
         * Anvil
         */
        @JvmField
        val ANVIL = VanillaLayout(
            7,
            0..2
        )

        /**
         * Beacon
         */
        @JvmField
        val BEACON = VanillaLayout(
            8,
            0..0
        )

        @JvmField
        val BLAST_FURNACE = VanillaLayout(
            9,
            0..2
        )

        @JvmField
        val BREWING_STAND = VanillaLayout(
            10,
            0..4
        )

        @JvmField
        val CRAFTING = VanillaLayout(
            11,
            0..9
        )

        @JvmField
        val ENCHANTMENT_TABLE = VanillaLayout(
            12,
            0..1
        )

        @JvmField
        val FURNACE = VanillaLayout(
            13,
            0..2
        )


        @JvmField
        val GRINDSTONE = VanillaLayout(
            14,
            0..2
        )

        @JvmField
        val HOPPER = VanillaLayout(
            15,
            0..4
        )

        @JvmField
        val LOOM = VanillaLayout(
            17,
            0..3
        )

        @JvmField
        val MERCHANT = VanillaLayout(
            18,
            0..2
        )

        @JvmField
        val SHULKER_BOX = VanillaLayout(
            19,
            0..26
        )

        @JvmField
        val SMOKER = VanillaLayout(
            20,
            0..2
        )

        @JvmField
        val CARTOGRAPHY = VanillaLayout(
            21,
            0..2
        )

        @JvmField
        val STONE_CUTTER = VanillaLayout(
            22,
            0..1
        )


        fun ofRows(rows: Int): VanillaLayout {
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