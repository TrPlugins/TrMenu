package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.impl.panel.PagedStandardPanel
import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute
import cc.trixey.mc.trmenu.invero.module.element.ElementDynamic
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/11/7 22:25
 */
class MappedElements {

    /**
     * Static-Slot Elements of this panel
     */
    private val absoluteElements = ConcurrentHashMap<Int, ElementAbsolute>()

    /**
     * Dynamic-Slot Elements of this panel
     */
    private val dynamicElements = CopyOnWriteArrayList<ElementDynamic>()

    /**
     * Slots that have been occupied by elements
     */
    internal val occupiedSlots: Set<Int>
        get() {
            return absoluteElements.keys + dynamicElements.flatMap { it.slots }
        }

    fun has(element: PanelElement): Boolean {
        return absoluteElements.containsValue(element) || dynamicElements.contains(element)
    }

    fun findElementSlots(element: ElementAbsolute): Set<Int> {
        return absoluteElements.filterValues { it == element }.keys
    }

    fun remove(slot: Int) {
        absoluteElements.remove(slot)
    }

    fun forDynamicElements(function: (ElementDynamic) -> Unit) {
        dynamicElements.forEach(function)
    }

    fun forAbsoluteElements(function: (ElementAbsolute) -> Unit) {
        absoluteElements.values.forEach(function)
    }

    operator fun get(slot: Int) = absoluteElements[slot] ?: dynamicElements.find { it.slots.contains(slot) }

    operator fun set(slot: Int, element: ElementAbsolute) = absoluteElements.set(slot, element)

    fun set(vararg slots: Int, element: ElementAbsolute) {
        slots.forEach { set(it, element) }
    }

    fun set(slots: Set<Int>, element: ElementAbsolute) {
        slots.forEach { set(it, element) }
    }

    operator fun plusAssign(element: ElementDynamic) {
        dynamicElements.add(element)
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

    /*
    Extensive operators
     */
    fun ElementDynamic.set(): ElementDynamic {
        this@MappedElements += this
        return this
    }

    fun ElementAbsolute.set(vararg slots: Int) {
        slots.forEach { this@MappedElements[it] = this }
    }

    fun PagedStandardPanel.getUnoccupiedSlots(excludeDefault: Boolean = true): Set<Int> {
        return (slots - this@MappedElements.occupiedSlots).let {
            if (excludeDefault) it - fallbackElements.occupiedSlots
            else it
        }
    }

}