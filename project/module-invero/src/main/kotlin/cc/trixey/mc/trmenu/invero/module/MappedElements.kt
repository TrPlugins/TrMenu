package cc.trixey.mc.trmenu.invero.module

import java.util.*

/**
 * @author Arasple
 * @since 2022/11/7 22:25
 */
class MappedElements {

    /**
     * Static-Slot Elements of this panel
     */
    private val elements = LinkedHashMap<Int, PanelElement>()

    /**
     * Dynamic-Slot Elements of this panel
     */
    private val dynamicElements = LinkedList<PanelElementDynamic>()

    fun findElement(panelElement: PanelElement): Set<Int> {
        return elements.filterValues { it == panelElement }.keys
    }

    fun setElement(relativeSlot: Int, element: PanelElement) {
        return if (!elements.containsKey(relativeSlot)) elements.set(relativeSlot, element)
        else throw UnsupportedOperationException("TODO PanelElement safely unregister")
    }

    fun getElement(relativeSlot: Int) = elements[relativeSlot]

    fun addElementDynamic(element: PanelElementDynamic) = dynamicElements.add(element)

    fun removeElementDynamic(element: PanelElementDynamic) = dynamicElements.remove(element)

    operator fun get(relativeSlot: Int) = getElement(relativeSlot)

    operator fun set(relativeSlot: Int, element: PanelElement) = setElement(relativeSlot, element)

    operator fun plusAssign(element: PanelElementDynamic) {
        dynamicElements += element
    }

    operator fun minusAssign(element: PanelElementDynamic) {
        dynamicElements -= element
    }

    fun forEach(function: (Int, PanelElement) -> Unit) {
        elements.forEach(function)
    }

    fun forEachDynamic(function: (PanelElementDynamic) -> Unit) {
        dynamicElements.forEach(function)
    }

}