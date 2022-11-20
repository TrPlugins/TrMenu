package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.MappedElements
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType

/**
 * @author Arasple
 * @since 2022/11/17 11:19
 *
 * 滚动元素存储在 ScrollColum 中，通过单例 ElementsMap 的设置来实现滚动元素
 */
abstract class BaseScrollPanel(
    scale: Pair<Int, Int>, pos: Int, weight: PanelWeight, val direction: ScrollDirection, val type: ScrollType
) : PanelInstance(scale, pos, weight) {

    /**
     * 元素
     */
    private val elementsMap = MappedElements()

    /**
     * 取得元素集
     */
    fun getElementsMap(): MappedElements {
        return elementsMap
    }

    override fun renderPanel() {
        forWindows {
            elementsMap.forElements {
                renderElement(this, it)
            }
        }
    }

    fun getBase(): MappedElements {
        return elementsMap
    }

    /**
     * 当前滚动定位
     */
    abstract var index: Int

    /**
     * 最大滚动定位
     */
    abstract val maxIndex: Int

    /**
     * 单个滚动栏目的容量
     */
    val columCapacity: Int by lazy {
        if (direction.isVertical) scale.first else scale.second
    }

    /**
     * 可视栏目总量
     *
     * 由 Panel 的尺寸和滚动方向决定
     */
    val columViewSize: Int by lazy {
        if (direction.isVertical) scale.second else scale.first
    }

    fun scroll(step: Int = 1) {
        index += step
    }

}
