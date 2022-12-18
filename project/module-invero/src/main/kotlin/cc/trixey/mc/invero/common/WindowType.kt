package cc.trixey.mc.invero.common

import org.bukkit.event.inventory.InventoryType

/**
 * @author Arasple
 * @since 2022/10/20
 *
 * 窗口使用的容器类型
 */
enum class WindowType(val vanillaId: String, val serialId: Int, slotsContainer: IntRange) {

    /**
     * A 1-row inventory, not used by the notchian server.
     */
    GENERIC_9X1("GENERIC_9x1", 0, 0..8),

    /**
     * A 2-row inventory, not used by the notchian server.
     */
    GENERIC_9X2("GENERIC_9x2", 1, 0..17),

    /**
     * General-purpose 3-row inventory. Used by Chest, minecart with chest, ender chest, and barrel
     */
    GENERIC_9X3("GENERIC_9x3", 2, 0..26),

    /**
     * A 4-row inventory, not used by the notchian server.
     */
    GENERIC_9X4("GENERIC_9x4", 3, 0..35),

    /**
     * A 5-row inventory, not used by the notchian server.
     */
    GENERIC_9X5("GENERIC_9x5", 4, 0..44),

    /**
     * General-purpose 6-row inventory, used by large chests.
     */
    GENERIC_9X6("GENERIC_9x6", 5, 0..53),

    /**
     * General-purpose 3-by-3 square inventory, used by Dispenser and Dropper
     */
    GENERIC_3X3("GENERIC_3x3", 6, 0..8),

    /**
     * Anvil
     */
    ANVIL("ANVIL", 7, 0..2),

    /**
     * Beacon
     */
    BEACON("BEACON", 8, 0..0),

    BLAST_FURNACE("BLAST_FURNACE", 9, 0..2),

    BREWING_STAND("BREWING_STAND", 10, 0..4),

    CRAFTING("CRAFTING", 11, 0..9),

    ENCHANTMENT("ENCHANTMENT", 12, 0..1),

    FURNACE("FURNACE", 13, 0..2),


    GRINDSTONE("GRINDSTONE", 14, 0..2),

    HOPPER("HOPPER", 15, 0..4),

    LOOM("LOOM", 17, 0..3),

    MERCHANT("MERCHANT", 18, 0..2),

    SHULKER_BOX("SHULKER_BOX", 19, 0..26),

    SMOKER("SMOKER", 20, 0..2),

    CARTOGRAPHY_TABLE("CARTOGRAPHY_TABLE", 21, 0..2),

    STONECUTTER("STONECUTTER", 22, 0..1);

    /**
     * Bukkit
     */
    val bukkitId by lazy { "minecraft:${bukkitType.name.lowercase()}" }

    /**
     * 玩家背包（不包括快捷栏）的展开槽位
     */
    val slotsPlayer by lazy { (slotsContainer.last + 1..slotsContainer.last + 27) }

    /**
     * 快捷栏的槽位
     */
    val slotsHotbar by lazy { (slotsPlayer.last + 1..slotsPlayer.last + 9) }

    /**
     * slotsPlayer + slotsHotbar
     */
    val slotsPlayerContents by lazy {
        slotsPlayer.first..slotsHotbar.last
    }

    /**
     * 全局视窗可用的槽位
     */
    val slotsEntireWindow by lazy { (0..slotsHotbar.last) }

    /**
     * 容器可用槽位
     */
    val slotsContainer by lazy { (0 until containerSize) }

    /**
     * 容器（不包括玩家背包）的大小
     */
    val containerSize by lazy { slotsContainer.last + 1 }

    /**
     * 视窗（包括玩家背包）的大小
     */
    val entireWindowSize by lazy { slotsHotbar.last + 1 }

    val width by lazy {
        if (isOrdinaryChest) 9
        // TODO width examine
        else 3
    }

    val bukkitType by lazy {
        InventoryType.valueOf(
            when (serialId) {
                0, 1, 2, 3, 4, 5 -> "CHEST"
                6 -> "HOPPER"
                7 -> "ANVIL"
                8 -> "BEACON"
                9 -> "BLAST_FURNACE"
                10 -> "BREWING"
                11 -> "CRAFTING"
                12 -> "ENCHANTING"
                13 -> "FURNACE"
                14 -> "GRINDSTONE"
                15 -> "HOPPER"
                16 -> "LOOM"
                17 -> "MERCHANT"
                18 -> "SHULKER_BOX"
                19 -> "SMOKER"
                20 -> "CARTOGRAPHY"
                21 -> "STONECUTTER"
                else -> "CHEST"
            }
        )
    }

    val isOrdinaryChest by lazy {
        this == GENERIC_9X1 || this == GENERIC_9X2 || this == GENERIC_9X3 || this == GENERIC_9X4 || this == GENERIC_9X5 || this == GENERIC_9X6
    }

    val rows: Int by lazy {
        when (this) {
            GENERIC_9X1 -> 1
            GENERIC_9X2 -> 2
            GENERIC_9X3 -> 3
            GENERIC_9X4 -> 4
            GENERIC_9X5 -> 5
            GENERIC_9X6 -> 6
            GENERIC_3X3 -> 3
            ANVIL -> 1
            BEACON -> TODO()
            BLAST_FURNACE -> TODO()
            BREWING_STAND -> TODO()
            CRAFTING -> TODO()
            ENCHANTMENT -> TODO()
            FURNACE -> TODO()
            GRINDSTONE -> TODO()
            HOPPER -> TODO()
            LOOM -> TODO()
            MERCHANT -> TODO()
            SHULKER_BOX -> TODO()
            SMOKER -> TODO()
            CARTOGRAPHY_TABLE -> TODO()
            STONECUTTER -> TODO()
        }
    }

    val scaleView: ScaleView by lazy {
        ScaleView(width to rows)
    }

    companion object {

        fun ofRows(rows: Int): WindowType {
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