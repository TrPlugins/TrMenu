package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.ScaleView
import cc.trixey.mc.invero.common.panel.BaseScrollPanel
import cc.trixey.mc.invero.common.panel.scroll.ScrollColum
import cc.trixey.mc.invero.common.panel.scroll.ScrollDirection
import cc.trixey.mc.invero.common.panel.scroll.ScrollType

/**
 * @author Arasple
 * @since 2022/11/17 11:17
 *
 * 标准滚动面板
 * 栏目仅接受静态元素
 */
open class ScrollStandardPanel(
    scale: ScaleView,
    weight: PanelWeight,
    direction: ScrollDirection = ScrollDirection(vertical = true),
    type: ScrollType = ScrollType.STOP
) : BaseScrollPanel(scale, weight, direction, type) {

    /**
     * @see BaseScrollPanel.index
     */
    override var index: Int = 0
        set(value) {
            val previous = field
            when {
                type.isStop -> if (value in 0..maxIndex) field = value
                type.isLoop -> field = if (value > maxIndex) 0 else if (value < 0) value + colums.size else value
            }
            if (previous != field) renderPanel()
        }

    /**
     * @see BaseScrollPanel.maxIndex
     *
     */
    override val maxIndex: Int
        get() {
            return when {
                type.isStop -> colums.size - (type.value).coerceAtMost(columViewSize)
                type.isLoop -> colums.lastIndex
                else -> error("maxIndex.get")
            }
        }

    /**
     * 可滚动的栏目元素
     */
    internal val colums = mutableListOf<ScrollColum>()

    fun addColum(function: ScrollColum.(indices: IntRange) -> Unit) {
        colums += ScrollColum(arrayOfNulls<Element?>(columCapacity)).also {
            function(it, 0 until columCapacity)
        }
    }

    override fun renderPanel() {
        val rendered = mutableSetOf<Int>()
        val apply = (index until index + columViewSize).map {
            if (type.isLoop && it > maxIndex) it - colums.size
            else it
        }

        apply.forEachIndexed { order, applyIndex ->
            val getSlot: (index: Int) -> Int = {
                if (direction.isVertical) order * scale.width + it
                else it * scale.width + order
            }
            val colum = colums.getOrNull(applyIndex)?.elements ?: kotlin.run {
                for (i in 0 until columCapacity) elementsMap.remove(getSlot(i))
                return@forEachIndexed
            }
            colum.forEachIndexed { y, element ->
                val slot = getSlot(y)

                if (element != null) {
                    elementsMap[slot] = element
                    rendered += slot
                } else {
                    elementsMap.remove(slot)
                }
            }
        }

        clearPanel(slots - rendered)
        super.renderPanel()
    }

}