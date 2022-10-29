package cc.trixey.mc.trmenu.legacy.invero.window

import cc.trixey.mc.trmenu.legacy.invero.window.InteractType.Mode.*
import org.bukkit.event.inventory.ClickType
import taboolib.common.platform.function.warning

/**
 * @author Arasple
 * @since 2022/10/21
 *
 * Middle drag check is only available for creative players
 */
enum class InteractType(val mode: Mode, val button: Int, val slot: Int = -1) {

    LEFT_CLICK(PICKUP, 0),

    RIGHT_CLICK(PICKUP, 1),

    LEFT_CLICK_OUTSIDE(PICKUP, 0, -999),

    RIGHT_CLICK_OUTSIDE(PICKUP, 1, -999),

    SHIFT_LEFT_CLICK(QUICK_MOVE, 0),

    SHIFT_RIGHT_CLICK(QUICK_MOVE, 1),

    NUMBER_KEY_1(SWAP, 0),

    NUMBER_KEY_2(SWAP, 1),

    NUMBER_KEY_3(SWAP, 2),

    NUMBER_KEY_4(SWAP, 3),

    NUMBER_KEY_5(SWAP, 4),

    NUMBER_KEY_6(SWAP, 5),

    NUMBER_KEY_7(SWAP, 6),

    NUMBER_KEY_8(SWAP, 7),

    NUMBER_KEY_9(SWAP, 8),

    OFFHAND_SWAP(SWAP, 40),

    MIDDLE_CLICK(CLONE, 2),

    DROP(THROW, 0),

    CONTROL_DROP(THROW, 1),

    LEFT_MOUSE_DRAG_STARTING(QUICK_CRAFT, 0, -999),

    LEFT_MOUSE_DRAG_ADD_SLOT(QUICK_CRAFT, 1),

    LEFT_MOUSE_DRAG_ENDING(QUICK_CRAFT, 2, -999),

    RIGHT_MOUSE_DRAG_STARTING(QUICK_CRAFT, 4, -999),

    RIGHT_MOUSE_DRAG_ADD_SLOT(QUICK_CRAFT, 5),

    RIGHT_MOUSE_DRAG_ENDING(QUICK_CRAFT, 6, -999),

    MIDDLE_MOUSE_DRAG_STARTING(QUICK_CRAFT, 8, -999),

    MIDDLE_MOUSE_DRAG_ADD_SLOT(QUICK_CRAFT, 9),

    MIDDLE_MOUSE_DRAG_ENDING(QUICK_CRAFT, 10, -999),

    DOUBLE_CLICK(PICKUP_ALL, 0);

    enum class Mode {

        PICKUP,

        QUICK_MOVE,

        SWAP,

        CLONE,

        THROW,

        QUICK_CRAFT,

        PICKUP_ALL

    }

    val isIgnoreSuggested by lazy {
        mode == QUICK_CRAFT && button in arrayOf(1, 2, 5, 6, 9, 10)
    }

    val bukkitClickType by lazy {
        when (this) {
            LEFT_CLICK -> ClickType.LEFT
            RIGHT_CLICK -> ClickType.RIGHT
            LEFT_CLICK_OUTSIDE -> ClickType.WINDOW_BORDER_LEFT
            RIGHT_CLICK_OUTSIDE -> ClickType.WINDOW_BORDER_RIGHT
            SHIFT_LEFT_CLICK -> ClickType.SHIFT_LEFT
            SHIFT_RIGHT_CLICK -> ClickType.SHIFT_RIGHT
            NUMBER_KEY_1, NUMBER_KEY_2, NUMBER_KEY_3, NUMBER_KEY_4, NUMBER_KEY_5, NUMBER_KEY_6, NUMBER_KEY_7, NUMBER_KEY_8, NUMBER_KEY_9 -> ClickType.NUMBER_KEY
            OFFHAND_SWAP -> ClickType.SWAP_OFFHAND
            MIDDLE_CLICK -> ClickType.MIDDLE
            DROP -> ClickType.DROP
            CONTROL_DROP -> ClickType.CONTROL_DROP
            LEFT_MOUSE_DRAG_STARTING -> ClickType.UNKNOWN
            LEFT_MOUSE_DRAG_ADD_SLOT -> ClickType.UNKNOWN
            LEFT_MOUSE_DRAG_ENDING -> ClickType.UNKNOWN
            RIGHT_MOUSE_DRAG_STARTING -> ClickType.UNKNOWN
            RIGHT_MOUSE_DRAG_ADD_SLOT -> ClickType.UNKNOWN
            RIGHT_MOUSE_DRAG_ENDING -> ClickType.UNKNOWN
            MIDDLE_MOUSE_DRAG_STARTING -> ClickType.UNKNOWN
            MIDDLE_MOUSE_DRAG_ADD_SLOT -> ClickType.UNKNOWN
            MIDDLE_MOUSE_DRAG_ENDING -> ClickType.UNKNOWN
            DOUBLE_CLICK -> ClickType.DOUBLE_CLICK
        }
    }

    val isRightClick by lazy {
        this == RIGHT_CLICK || this == SHIFT_RIGHT_CLICK
    }

    val isLeftClick by lazy {
        this == LEFT_CLICK || this == SHIFT_LEFT_CLICK || this == DOUBLE_CLICK
    }

    val isShiftClick by lazy {
        this == SHIFT_LEFT_CLICK || this == SHIFT_RIGHT_CLICK
    }

    val isKeyboardClick by lazy {
        isNumberKeyClick || this == DROP || this == CONTROL_DROP
    }

    val isNumberKeyClick by lazy {
        this.name.startsWith("NUMBER_KEY") || this == OFFHAND_SWAP
    }

    val isDoubleClick by lazy {
        this == DOUBLE_CLICK
    }

    val isCreativeAction by lazy {
        this.name.contains("MIDDLE")
    }

    val isItemMoveable by lazy {
        isKeyboardClick || isShiftClick || isCreativeAction || isDoubleClick
    }

    /*
    # Paiting interaction

    Starting from version 1.5, “painting mode” is available for use in inventory windows.
    It is done by picking up stack of something (more than 1 item),
    then holding mouse button (left, right or middle) and dragging held stack over empty (or same type in case of right button) slots.
    In that case client sends the following to server after mouse button release (omitting first pickup packet which is sent as usual):
    packet with mode 5, slot -999, button (0 for left | 4 for right);
    packet for every slot painted on, mode is still 5, button (1 | 5);
    packet with mode 5, slot -999, button (2 | 6);
    If any of the painting packets other than the “progress” ones are sent out of order
    (for example, a start, some slots, then another start; or a left-click in the middle)
    the painting status will be reset.
     */

    companion object {

        fun find(mode: Mode, button: Int, slot: Int = -1): InteractType? {
            return InteractType.values().find {
                it.mode == mode && it.button == button && (it.slot == -1 || it.slot == slot)
            }.also {
                if (it == null) {
                    warning("Not found interact type. Mode: $mode, Button: $button, Slot: $slot")
                }
            }
        }

    }

}