package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 10:59
 */
interface Panel : Parentable {

    /**
     * The windows to which this panel applied
     *
     * @attention do not abuse this property
     */
    val windows: List<Window>
        get() {
            return WindowHolder.runningWindows.filter { it.panels.contains(this) }
        }

    /**
     * Scale of this panel
     * Width and height
     */
    val scale: Pair<Int, Int>

    /**
     * Relative Available Slots
     */
    val slots: List<Int>

    /**
     * The top-left slot of this panel's real position
     */
    val pos: Int

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
     * Width: (RelativeSlot: ActualWindowSlot)
     */
    fun getSlotsMap(window: Window): Map<Int, Int>

    fun postRender(function: Panel.() -> Unit) {
        function(this)
    }

    fun setElement(relativeSlot: Int, element: PanelElement) {
        return if (!elements.containsKey(relativeSlot)) elements.set(relativeSlot, element)
        else throw UnsupportedOperationException("TODO PanelElement safely unregister")
    }

    fun getElement(relativeSlot: Int) = elements[relativeSlot]

    fun addElementDynamic(element: PanelElementDynamic) = dynamicElements.add(element)

    fun removeElementDynamic(element: PanelElementDynamic) = dynamicElements.remove(element)

}