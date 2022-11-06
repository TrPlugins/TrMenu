package cc.trixey.mc.trmenu.invero.module

import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/1 22:00
 */
interface PanelElement {

    /**
     * The panel to which this element belongs to
     */
    val panel: Panel

    val panelElements: LinkedHashMap<Int, PanelElement>
        get() {
            return panel.elements
        }

    val panelDynamicElements: LinkedList<PanelElementDynamic>
        get() {
            return panel.dynamicElements
        }

    val windows: LinkedList<Window>
        get() {
            return panel.windows
        }

    val relativeSlots: List<Int>?

    fun Window.slotMap() = panel.getSlotsMap(this)

    fun forWindows(function: Window.() -> Unit) = panel.forWindows(function)

    fun handleClick(e: InventoryClickEvent)

    fun render()

}