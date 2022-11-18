package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.MappedElements
import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute
import cc.trixey.mc.trmenu.invero.module.element.ElementDynamic

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

    override fun renderPanel(window: Window) {
        elementsMap.forAbsoluteElements {
            it.renderElement(window)
        }
        elementsMap.forDynamicElements {
            it.renderElement(window)
        }
    }

    /**
     * Elements of this panel
     */
    val elementsMap = MappedElements()

    /**
     * Get occupied slots (relative)
     */
    fun getOccupiedSlots(): Set<Int> {
        return elementsMap.occupiedSlots
    }

    /**
     * Get unoccupied slots
     */
    fun getUnoccupiedSlots(): Set<Int> {
        return slots - elementsMap.occupiedSlots
    }

    /**
     * Set panel's element
     */
    fun set(slot: Int, element: ElementAbsolute) {
        elementsMap[slot] = element
    }

    /**
     * Set panel's element on multiple slots
     */
    fun set(slots: Array<Int>, element: ElementAbsolute) {
        slots.forEach {
            set(it, element)
        }
    }

    /**
     * Set panel's element on multiple slots
     */
    fun set(slots: IntRange, element: ElementAbsolute) {
        slots.forEach {
            set(it, element)
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
        set(slots.toSet())
    }

    fun ElementAbsolute.set(slots: Set<Int>) {
        slots.forEach { set(it, this) }
    }

}