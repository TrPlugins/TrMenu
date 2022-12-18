package cc.trixey.mc.invero.common.panel

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.ScaleView

/**
 * @author Arasple
 * @since 2022/11/6 16:51
 *
 * 基础的翻页 Panel 模型
 */
abstract class BasePagedPanel(
    scale: ScaleView,
    weight: PanelWeight
) : PanelInstance(scale, weight) {

    /**
     * 当前页码
     */
    abstract var pageIndex: Int

    /**
     * 最大页码
     */
    abstract val maxPageIndex: Int

    /**
     * 切换下一页
     */
    open fun nextPage() = pageIndex++

    /**
     * 切换上一页
     */
    open fun previousPage() = pageIndex--

    /**
     * 自定义切页
     */
    open fun shiftPage(value: Int) {
        pageIndex += value
    }

    /**
     * 切换到指定页码
     */
    open fun switchPage(page: Int) {
        pageIndex = page
    }

    /**
     * 取得指定页码已占用的槽位
     */
    abstract fun getOccupiedSlots(page: Int): Set<Int>

    /**
     * 取得指定页码还未被占用的槽位
     */
    fun getUnoccupiedSlots(page: Int) = slots - getOccupiedSlots(page)

}