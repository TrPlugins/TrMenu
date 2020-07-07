package me.arasple.mc.trmenu.api.inventory

/**
 * @author Arasple
 * @date 2020/3/22 10:23
 */
enum class MenuClickType {

    /**
     * 左键点击
     */
    LEFT,

    /**
     * 按住 Shift 并左键点击
     */
    SHIFT_LEFT,

    /**
     * 右键点击
     */
    RIGHT,

    /**
     * 按住 Shift 并右键点击
     */
    SHIFT_RIGHT,

    /**
     * 中键点击 (摁住鼠标滚轮)
     */
    MIDDLE,

    /**
     * 按住 Shift 并中键点击
     */
    DOUBLE_CLICK,

    /**
     * 丢弃键 (默认为 Q)
     */
    DROP,

    /**
     * Ctrl + 丢弃键
     */
    CONTROL_DROP,

    /**
     * 创造模式 + 中键
     */
    CREATIVE,

    /**
     * 数字键 1 - 9
     */
    NUMBER_KEY,

    /**
     * 数字键 1
     */
    NUMBER_KEY_1,

    /**
     * 数字键 2
     */
    NUMBER_KEY_2,

    /**
     * 数字键 3
     */
    NUMBER_KEY_3,

    /**
     * 数字键 4
     */
    NUMBER_KEY_4,

    /**
     * 数字键 5
     */
    NUMBER_KEY_5,

    /**
     * 数字键 6
     */
    NUMBER_KEY_6,

    /**
     * 数字键 7
     */
    NUMBER_KEY_7,

    /**
     * 数字键 8
     */
    NUMBER_KEY_8,

    /**
     * 数字键 9
     */
    NUMBER_KEY_9,

    /**
     * 点击容器左侧区域
     */
    WINDOW_BORDER_LEFT,

    /**
     * 点击容器右侧区域
     */
    WINDOW_BORDER_RIGHT,

    /**
     * 所有触发方式
     */
    ALL;

    fun isRightClick(): Boolean = this == RIGHT || this == SHIFT_RIGHT

    fun isLeftClick(): Boolean = this == LEFT || this == SHIFT_LEFT || this == DOUBLE_CLICK || this == CREATIVE

    fun isShiftClick(): Boolean = this == SHIFT_LEFT || this == SHIFT_RIGHT

    fun isKeyboardClick(): Boolean = isNumberKeyClick() || this == DROP || this == CONTROL_DROP

    fun isNumberKeyClick(): Boolean = this.name.startsWith(NUMBER_KEY.name)

    fun isDoubleClick(): Boolean = this == DOUBLE_CLICK

    fun isCreativeAction(): Boolean = this == MIDDLE || this == CREATIVE

    fun isItemMoveable(): Boolean = isKeyboardClick() || isShiftClick() || isCreativeAction() || isDoubleClick()

    companion object {

        fun match(string: String): MenuClickType = values().firstOrNull { it.name.equals(string.toUpperCase().replace(" ", "_"), true) } ?: ALL

        fun matches(string: String): Set<MenuClickType> = mutableSetOf<MenuClickType>().let { set ->
            string.split(',', ';').forEach { set.add(match(it)) }
            return@let set
        }

    }

}