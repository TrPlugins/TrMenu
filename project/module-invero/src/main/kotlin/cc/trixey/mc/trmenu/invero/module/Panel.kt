package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import cc.trixey.mc.trmenu.invero.module.`object`.MappedSlots
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 10:59
 */
interface Panel : Parentable {

    /**
     * Windows that use this panel
     */
    val windows: LinkedList<Window>

    /**
     * Scale of this panel
     * Width and height
     */
    val scale: Pair<Int, Int>

    /**
     * Relative slots of this panel
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
     * Claimed Slots Map
     *
     * Width: MappedSlots(Actual:Relative)
     */
    val slotsMap: LinkedHashMap<Int, MappedSlots>

    /**
     * Mapped slots
     * (ActualWindowSlot:RelativeSlot)
     */
    /**
     * Get the slotsmap for a window
     */
    fun getSlotsMap(parent: Parentable): MappedSlots

    /**
     * Check if this element is current renderable
     */
    fun isRenderable(element: PanelElement): Boolean

    /**
     * Render this panel to a specifical window
     */
    fun renderPanel(window: Window)

    /**
     * Unregister from a window
     */
    fun unregisterWindow(window: Window)

    /**
     * Register to a window
     */
    fun registerWindow(window: Window)

    /**
     * forEach Windows
     */
    fun forWindows(function: Window.() -> Unit)

    /**
     * Render this panel to all windows
     */
    fun renderAll()

    /**
     * Handle click event
     */
    fun handleClick(window: Window, e: InventoryClickEvent)

    /**
     * Handle items collect event
     */
    fun handleItemsCollect(window: Window, e: InventoryClickEvent)

    /**
     * Handle items move event
     */
    fun handleItemsMove(window: Window, e: InventoryClickEvent)

}