package cc.trixey.mc.trmenu.invero.module

import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 10:59
 */
interface Panel : Parentable {

    /**
     * The window to which this panel belongs
     */
    val window: Window

    /**
     * Size of this panel
     * Width and height
     */
    val size: Pair<Int, Int>

    /**
     * The top-left slot of this panel's real position
     */
    val posMark: Int

    /**
     * Panel weight
     * Weight can define the render priority of the panel's elements
     */
    var weight: PanelWeight

    /**
     * Panel Elements
     * (RelativeSlot: Element)
     */
    val elements: LinkedHashMap<Int, PanelElement>

    /**
     * Panel dynamic elements
     * Referring to those elements whose position are not definite and changable
     */
    val dynamicElements: LinkedList<PanelElementDynamic>

    /**
     * Mapped slots
     * RelativeSlot: ActualWindowSlot
     */
    val slotsMap: LinkedHashMap<Int, Int>

    override fun getParent() = window

    /**
     * Set the weight of this panel
     *
     * @see PanelWeight
     */
    fun weight(weight: PanelWeight) {
        this.weight = weight
    }

    fun postRender(function: Panel.() -> Unit) {
        function(this)
    }

    fun setElement(relativeSlot: Int, element: PanelElement) {
        return if (!elements.containsKey(relativeSlot))
            elements.set(relativeSlot, element)
        else throw UnsupportedOperationException("TODO PanelElement safely unregister")
    }

    fun getElement(relativeSlot: Int) = elements[relativeSlot]

    fun addElementDynamic(element: PanelElementDynamic) = dynamicElements.add(element)

    fun removeElementDynamic(element: PanelElementDynamic) = dynamicElements.remove(element)

}