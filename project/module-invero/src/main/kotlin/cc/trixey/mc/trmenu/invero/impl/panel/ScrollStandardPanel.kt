package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.*
import cc.trixey.mc.trmenu.invero.module.base.BaseScrollPanel

/**
 * @author Arasple
 * @since 2022/11/17 11:17
 *
 * TODO 未完成
 */
class ScrollStandardPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight,
    direction: ScrollDirection = ScrollDirection(vertical = true),
    scrollType: ScrollType = ScrollType.STOP
) : BaseScrollPanel(scale, pos, weight, direction, scrollType) {

    override fun renderPanel(window: Window) {
        index + capacity

        for (i in index..index + capacity) {
            colums[i].elements.forEachIndexed { index, element ->
                set(window.locate(i, index), element)
            }
        }

        super.renderPanel(window)
    }

    /**
     * @see BaseScrollPanel.index
     */
    override var index: Int = 0
        set(value) {
            if (value + capacity <= colums.lastIndex) {
                field = value
            } else {
                error("Index $value set failed")
            }
        }

    /**
     * 可滚动的栏目元素
     */
    private val colums = mutableListOf<ColumElements>()

    /**
     * 同时支持显示栏目的个数
     */
    private val capacity by lazy {
        if (direction.isVertical) scale.second else scale.first
    }

    /**
     * 每个栏目包含的至多元素个数
     */
    private val columSize by lazy {
        if (direction.isVertical) scale.first else scale.second
    }

    /**
     * @see BaseScrollPanel.scroll
     */
    override fun scroll(step: Int) {
        index += step
    }

    private fun Window.locate(line: Int, index: Int): Int {
        if (direction.isVertical) {
            (line * columSize) + index
        } else {
            // TODO
        }


        return getSlotsMap(this).getActual((line * columSize) + index)
    }

    operator fun plusAssign(colum: ColumElements) {
        colums += colum.safeCheck(columSize)
    }

    operator fun minusAssign(colum: ColumElements) {
        colums -= colum
    }

}