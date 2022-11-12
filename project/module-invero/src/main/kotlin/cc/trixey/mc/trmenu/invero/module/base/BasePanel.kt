package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelElementAbsolute
import cc.trixey.mc.trmenu.invero.module.PanelElementDynamic
import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight

/**
 * @author Arasple
 * @since 2022/11/6 16:47
 *
 * BasePanel is the most basic panel, which supports only one page of ordinary elements
 */
abstract class BasePanel(scale: Pair<Int, Int>, pos: Int, weight: PanelWeight) : PanelInstance(scale, pos, weight) {

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
    fun setElement(relativeSlot: Int, element: PanelElementAbsolute) {
        elementsMap[relativeSlot] = element
    }

    /**
     * Get element of this panel
     */
    fun getAbsoluteElement(relativeSlot: Int): PanelElementAbsolute? {
        return elementsMap[relativeSlot]
    }

    /**
     * Add dynamic element to this panel
     */
    fun addDynamicElement(element: PanelElementDynamic) {
        elementsMap += element
    }

    /**
     * Remove dynamic element to this panel
     */
    fun removeDynamicElement(element: PanelElementDynamic) {
        elementsMap -= element
    }

    /**
     * Render this panel to a specific window
     */
    override fun render(window: Window) {
        elementsMap.forAbsoluteElements { element ->
            element.render(window)
        }
        elementsMap.forDynamicElements {
            // TODO Render dynamic
        }
    }

}