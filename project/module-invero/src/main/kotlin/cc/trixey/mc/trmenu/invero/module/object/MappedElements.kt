package cc.trixey.mc.trmenu.invero.module.`object`

import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute
import cc.trixey.mc.trmenu.invero.module.element.ElementDynamic
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/7 22:25
 */
class MappedElements {

    /**
     * Static-Slot Elements of this panel
     */
    private val absoluteElements = LinkedHashMap<Int, ElementAbsolute>()

    /**
     * Dynamic-Slot Elements of this panel
     */
    private val dynamicElements = LinkedList<ElementDynamic>()

    val slotsOccupied: Set<Int>
        get() {
            return absoluteElements.keys + dynamicElements.flatMap { it.slots }
        }

    fun hasElement(element: PanelElement): Boolean {
        return absoluteElements.containsValue(element) || dynamicElements.contains(element)
    }

    fun findElementSlots(panelElement: ElementAbsolute): Set<Int> {
        return absoluteElements.filterValues { it == panelElement }.keys
    }

    fun setElement(element: ElementAbsolute, relativeSlot: Int) {
        return if (!absoluteElements.containsKey(relativeSlot)) absoluteElements.set(relativeSlot, element)
        else {
            // TODO Safely unregister element
//            error("PanelElement at $relativeSlot is already registered")
            absoluteElements[relativeSlot] = element
        }
    }

    fun setElement(relativeSlot: Int, element: ElementAbsolute) {
        return setElement(element, relativeSlot)
    }

    fun removeElement(relativeSlot: Int) {
         absoluteElements.remove(relativeSlot)
    }

    fun ElementDynamic.set(): ElementDynamic {
        this@MappedElements += this
        return this
    }

    fun ElementAbsolute.set(vararg slots: Int) {
        slots.forEach { setElement(this, it) }
    }

    fun getAbsoluteElement(relativeSlot: Int) = absoluteElements[relativeSlot]

    fun getDynamicElement(relativeSlot: Int) = dynamicElements.find { it.slots.contains(relativeSlot) }

    operator fun get(relativeSlot: Int) = getAbsoluteElement(relativeSlot) ?: getDynamicElement(relativeSlot)

    operator fun set(relativeSlot: Int, element: ElementAbsolute) = setElement(element, relativeSlot)

    fun forDynamicElements(function: (ElementDynamic) -> Unit) {
        dynamicElements.forEach(function)
    }

    fun forAbsoluteElements(function: (ElementAbsolute) -> Unit) {
        absoluteElements.values.forEach(function)
    }

    operator fun minusAssign(element: PanelElement) {
        if (element is ElementAbsolute) {
            val keys = absoluteElements.filterValues { it == element }.keys
            keys.forEach {
                absoluteElements.remove(it)
            }
        } else if (element is ElementDynamic) {
            dynamicElements.remove(element)
        }
    }

    operator fun plusAssign(element: ElementDynamic) {
        dynamicElements.add(element)
    }

}