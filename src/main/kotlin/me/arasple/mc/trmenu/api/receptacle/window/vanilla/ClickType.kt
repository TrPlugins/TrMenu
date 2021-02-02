package me.arasple.mc.trmenu.api.receptacle.window.vanilla

/**
 * @author Arasple
 * @date 2020/12/5 22:01
 */
enum class ClickType(private val mode: Int, private val button: Int) {

    ALL(-1, -1),

    LEFT(0, 0),

    RIGHT(0, 1),

    SHIFT_LEFT(1, 0),

    SHIFT_RIGHT(1, 1),

    OFFHAND(2, 40),

    NUMBER_KEY_1(2, 0),

    NUMBER_KEY_2(2, 1),

    NUMBER_KEY_3(2, 2),

    NUMBER_KEY_4(2, 3),

    NUMBER_KEY_5(2, 4),

    NUMBER_KEY_6(2, 5),

    NUMBER_KEY_7(2, 6),

    NUMBER_KEY_8(2, 7),

    NUMBER_KEY_9(2, 8),

    MIDDLE(3, 2),

    // clicked Item will be empty
    DROP(4, 0),

    CONTROL_DROP(4, 1),

    ABROAD_LEFT_EMPTY(4, 0),

    ABROAD_RIGHT_EMPTY(4, 1),

    ABROAD_LEFT_ITEM(0, 0),

    ABROAD_RIGHT_ITEM(0, 1),

    LEFT_MOUSE_DRAG_ADD(5, 1),

    RIGHT_MOUSE_DRAG_ADD(5, 5),

    // Starting middle mouse drag, only defined for creative players in non-player inventories. (Note: the vanilla client will still incorrectly send this for non-creative players - see MC-46584)
    MIDDLE_MOUSE_DRAG_ADD(5, 9),

    DOUBLE_CLICK(6, 0),

    UNKNOWN(-1, -1);

//    LEFT_MOUSE_DRAG_ENDING(5, 2),
//    LEFT_MOUSE_DRAG_STARTING(5, 0), // -999
//    RIGHT_MOUSE_DRAG_STARTING(5, 4), // -999
//    RIGHT_MOUSE_DRAG_ENDING(5, 6),
//    MIDDLE_MOUSE_DRAG_STARTING(5, 8), // -999
//    MIDDLE_MOUSE_DRAG_ENDING(5, 10),

    fun equals(mode: Int, button: Int): Boolean {
        return this.mode == mode && this.button == button
    }

    fun isRightClick(): Boolean = this == RIGHT || this == SHIFT_RIGHT

    fun isLeftClick(): Boolean = this == LEFT || this == SHIFT_LEFT || this == DOUBLE_CLICK

    fun isShiftClick(): Boolean = this == SHIFT_LEFT || this == SHIFT_RIGHT

    fun isKeyboardClick(): Boolean = isNumberKeyClick() || this == DROP || this == CONTROL_DROP

    fun isNumberKeyClick(): Boolean = this.name.startsWith("NUMBER_KEY") || this == OFFHAND

    fun isDoubleClick(): Boolean = this == DOUBLE_CLICK

    fun isCreativeAction(): Boolean = this == MIDDLE || this == MIDDLE_MOUSE_DRAG_ADD

    fun isItemMoveable(): Boolean = isKeyboardClick() || isShiftClick() || isCreativeAction() || isDoubleClick()

    companion object {

        private fun matchesSignle(string: String): ClickType {
            return values().find { it.name.equals(string, true) } ?: ALL
        }

        fun matches(string: String): Set<ClickType> = mutableSetOf<ClickType>().run {
            string.split(',', ';').forEach { add(matchesSignle(it)) }
            this
        }

        fun from(mode: String, button: Int, slot: Int = -1): ClickType? {
            return from(modes.indexOf(mode), button, slot)
        }

        private fun from(mode: Int, button: Int, slot: Int = -1): ClickType? {
            if (slot == -999) {
                return when {
                    LEFT.equals(mode, button) -> ABROAD_LEFT_ITEM
                    RIGHT.equals(mode, button) -> ABROAD_RIGHT_ITEM
                    ABROAD_LEFT_EMPTY.equals(mode, button) -> ABROAD_LEFT_EMPTY
                    ABROAD_RIGHT_EMPTY.equals(mode, button) -> ABROAD_RIGHT_EMPTY
                    else -> UNKNOWN
                }
            }
            return values().find { it.equals(mode, button) }
        }

        private val modes = arrayOf(
            "PICKUP",
            "QUICK_MOVE",
            "SWAP",
            "CLONE",
            "THROW",
            "QUICK_CRAFT",
            "PICKUP_ALL"
        )

    }

}