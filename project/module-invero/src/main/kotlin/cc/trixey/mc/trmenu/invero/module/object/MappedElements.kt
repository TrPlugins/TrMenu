package cc.trixey.mc.trmenu.invero.module.`object`

import cc.trixey.mc.trmenu.invero.module.PanelElement
import cc.trixey.mc.trmenu.invero.module.PanelElementAbsolute
import cc.trixey.mc.trmenu.invero.module.PanelElementDynamic
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/7 22:25
 */
class MappedElements {

    /**
     * Static-Slot Elements of this panel
     */
    private val absoluteElements = LinkedHashMap<Int, PanelElementAbsolute>()

    /**
     * Dynamic-Slot Elements of this panel
     */
    private val dynamicElements = LinkedList<PanelElementDynamic>()

    val slotsOccupied: Set<Int>
        get() {
            // TODO DynamicElement Support
            return absoluteElements.keys
        }

    fun findElementSlots(panelElement: PanelElementAbsolute): Set<Int> {
        return absoluteElements.filterValues { it == panelElement }.keys
    }

    fun setElement(relativeSlot: Int, element: PanelElementAbsolute) {
        return if (!absoluteElements.containsKey(relativeSlot)) absoluteElements.set(relativeSlot, element)
        else throw UnsupportedOperationException("TODO PanelElement safely unregister")
    }

    fun getAbsoluteElement(relativeSlot: Int) = absoluteElements[relativeSlot]

    operator fun get(relativeSlot: Int) = getAbsoluteElement(relativeSlot)

    operator fun set(relativeSlot: Int, element: PanelElementAbsolute) = setElement(relativeSlot, element)

    fun forDynamicElements(function: (PanelElementDynamic) -> Unit) {
        dynamicElements.forEach(function)
    }

    fun forAbsoluteElements(function: (PanelElementAbsolute) -> Unit) {
        absoluteElements.values.forEach(function)
    }

    operator fun minusAssign(element: PanelElement) {
        if (element is PanelElementAbsolute) {
            val keys = absoluteElements.filterValues { it == element }.keys
            keys.forEach {
                absoluteElements.remove(it)
            }
        } else if (element is PanelElementDynamic) {
            dynamicElements.remove(element)
        }
    }

    operator fun plusAssign(element: PanelElementDynamic) {
        dynamicElements.add(element)
    }

}