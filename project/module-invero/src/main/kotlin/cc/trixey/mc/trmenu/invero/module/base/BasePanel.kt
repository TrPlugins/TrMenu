package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute
import cc.trixey.mc.trmenu.invero.module.element.ElementDynamic
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight

/**
 * @author Arasple
 * @since 2022/11/6 16:47
 *
 * BasePanel is the most basic panel, which supports only one page of ordinary elements
 */
abstract class BasePanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    /**
     * Elements of this panel
     */
    internal val elementsMap = MappedElements()

    /**
     * @see PanelInstance.slotsOccupied
     */
    override val slotsOccupied: Set<Int>
        get() = elementsMap.slotsOccupied

    /**
     * @see PanelInstance.slotsUnoccupied
     */
    override val slotsUnoccupied: List<Int>
        get() = slots - slotsOccupied

    /**
     * Set panel's element
     */
    fun setElement(relativeSlot: Int, element: ElementAbsolute) {
        elementsMap[relativeSlot] = element
    }

    /**
     * Set panel's element on multiple slots
     */
    fun setElement(relativeSlots: Array<Int>, element: ElementAbsolute) {
        relativeSlots.forEach {
            setElement(it, element)
        }
    }

    /**
     * Set panel's element on multiple slots
     */
    fun setElement(relativeSlots: IntRange, element: ElementAbsolute) {
        relativeSlots.forEach {
            setElement(it, element)
        }
    }

    /**
     * Add a dynamic element to this panel
     */
    fun addDynamicElement(element: ElementDynamic) {
        elementsMap += element
    }

    fun ElementDynamic.add(): ElementDynamic {
        elementsMap += this
        return this
    }

    fun ElementAbsolute.set(vararg slots: Int) {
        slots.forEach { setElement(it, this) }
    }

    /**
     * Render this panel to a specific window
     */
    override fun renderPanel(window: Window) {
        elementsMap.forAbsoluteElements {
            it.renderElement(window)
        }
        elementsMap.forDynamicElements {
            it.renderElement(window)
        }
    }

}