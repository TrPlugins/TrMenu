package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.ItemProvider
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.Window
import cc.trixey.mc.invero.common.base.BaseScrollPanel
import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.scroll.ScrollColum
import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType
import cc.trixey.mc.invero.common.scroll.ScrollType.*
import cc.trixey.mc.invero.util.distinguishMark

/**
 * @author Arasple
 * @since 2022/11/17 11:17
 */
class ScrollStandardPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight,
    direction: ScrollDirection = ScrollDirection(vertical = true),
    type: ScrollType = STOP
) : BaseScrollPanel(scale, pos, weight, direction, type) {

    /**
     * @see BaseScrollPanel.index
     */
    override var index: Int = 0
        set(value) {
            if (value in 0..maxIndex) {
                field = value
                renderPanel()
            }
        }

    /**
     * @see BaseScrollPanel.maxIndex
     */
    override val maxIndex: Int by lazy {
        when (type) {
            STOP -> colums.size - columViewSize
            LOOP -> -1
            BLANK -> colums.size - type.value
        }
    }

    /**
     * 可滚动的栏目元素
     */
    private val colums = mutableListOf<ScrollColum>()

    fun colum(function: ScrollColum.() -> Unit) {
        colums += ScrollColum(arrayOfNulls<ElementAbsolute?>(9)).also(function)
    }

    override fun renderPanel() {
        val rendered = mutableSetOf<Int>()

        for (x in index until index + columViewSize) {
            colums[x].elements.forEachIndexed { y, element ->
                if (element != null)
                    if (direction.isVertical) {
                        val slot = (x - index) * scale.first + y
                        getElementsMap()[slot] = element
                        rendered += slot
                    } else {
                        TODO("support horizontal")
                    }
            }
        }
        clearPanel(slots - rendered)

        super.renderPanel()
    }

    override fun renderElement(window: Window, element: Element) {
        if (!isRenderable(element)) return

        val slotMap = getSlotsMap(window)
        if (element is ItemProvider) {
            val itemStack = element.get()
            getElementsMap().findSlots(element).forEach {
                val slot = slotMap.getAbsolute(it)
                window.inventorySet[slot] = itemStack.distinguishMark(slot)
            }
        }
    }

}