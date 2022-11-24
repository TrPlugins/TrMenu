package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/24 12:23
 *
 * Interface for Scale(width, height)
 * Used by Window & Panel to better calculate the slots
 */
interface Scale {

    /**
     * 当前对象的定位点
     *
     * -1 for Window
     */
    val position: Int

    /**
     * 当前对象的尺寸宽带
     */
    val width: Int

    /**
     * 当前对象的尺寸高度
     */
    val height: Int

    /**
     * 当前对象的尺寸面积
     */
    val area: Int
        get() = width * height

    /**
     * 获取该对象允许的槽位
     * （相对于自己）
     */
    val slots: List<Int>

    /**
     * 定位本对象内的某个槽位的行列
     */
    fun locate(slot: Int): Pair<Int, Int>

    /**
     * 根据当前对象的（相对槽位）映射父级的槽位
     */
    fun getUpperSlot(upper: Scale, slot: Int): Int

}