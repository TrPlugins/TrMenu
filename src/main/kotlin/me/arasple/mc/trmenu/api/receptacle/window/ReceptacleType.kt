package me.arasple.mc.trmenu.api.receptacle.window

/**
 * @author Arasple
 * @date 2020/11/29 10:39
 *
 * reference https://wiki.vg/Inventory#Windows
 */
enum class ReceptacleType(
    val vanillaType: String,
    val vanillaId: Int,
    val slotRange: IntRange
) {

    /**
     *
     * A 1-row inventory, not used by the notchian server.
     */
    GENERIC_9X1(
        "minecraft:generic_9x1",
        0,
        0..8
    ),

    /**
     * A 2-row inventory, not used by the notchian server.
     */
    GENERIC_9X2(
        "minecraft:generic_9x2",
        1,
        0..17
    ),

    /**
     * General-purpose 3-row inventory. Used by Chest, minecart with chest, ender chest, and barrel
     */
    GENERIC_9X3(
        "minecraft:generic_9x3",
        2,
        0..26
    ),

    /**
     * A 4-row inventory, not used by the notchian server.
     */
    GENERIC_9X4(
        "minecraft:generic_9x4",
        3,
        0..35
    ),

    /**
     * A 5-row inventory, not used by the notchian server.
     */
    GENERIC_9X5(
        "minecraft:generic_9x5",
        4,
        0..44
    ),

    /**
     * General-purpose 6-row inventory, used by large chests.
     */
    GENERIC_9X6(
        "minecraft:generic_9x6",
        5,
        0..53
    ),

    /**
     * General-purpose 3-by-3 square inventory, used by Dispenser and Dropper
     */
    GENERIC_3X3(
        "minecraft:generic_3x3",
        6,
        0..8
    ),

    /**
     * Anvil
     */
    ANVIL(
        "minecraft:anvil",
        7,
        0..2
    ),

    /**
     * Beacon
     */
    BEACON(
        "minecraft:beacon",
        8,
        0..0
    ),

    BLAST_FURNACE(
        "minecraft:blast_furnace",
        9,
        0..2
    ),

    BREWING_STAND(
        "minecraft:brewing_stand",
        10,
        0..4
    ),

    CRAFTING(
        "minecraft:crafting",
        11,
        0..9
    ),

    ENCHANTMENT_TABLE(
        "minecraft:enchantment",
        12,
        0..1
    ),

    FURNACE(
        "minecraft:furnace",
        13,
        0..2
    ),


    GRINDSTONE(
        "minecraft:grindstone",
        14,
        0..2
    ),

    HOPPER(
        "minecraft:hopper",
        15,
        0..4
    ),


//    LECTERN("minecraft:lectern"),

    LOOM(
        "minecraft:loom",
        17,
        0..3
    ),

    MERCHANT(
        "minecraft:merchant",
        18,
        0..2
    ),

    SHULKER_BOX(
        "minecraft:shulker_box",
        19,
        0..26
    ),

    SMOKER(
        "minecraft:smoker",
        20,
        0..2
    ),

    CARTOGRAPHY(
        "minecraft:cartography",
        21,
        0..2
    ),

    STONECUTTER(
        "minecraft:stonecutter",
        22,
        0..1
    );

    /**
     * Main inventory slot range
     */
    val mainInvSlotRange = slotRange.last + 1..slotRange.last + 27

    val mainInvSlots = mainInvSlotRange.toList()

    /**
     * Hotbar slot range
     */
    val hotBarSlotRange = mainInvSlotRange.last + 1..mainInvSlotRange.last + 9

    val hotBarSlots = hotBarSlotRange.toList()

    /**
     * Container size
     */
    val containerSize = slotRange.last - slotRange.first

    /**
     * Total size (with hotbar & mainInv)
     * Not slot index
     */
    val totalSize = containerSize + 37

    companion object {

        fun ofRows(rows: Int): ReceptacleType {
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