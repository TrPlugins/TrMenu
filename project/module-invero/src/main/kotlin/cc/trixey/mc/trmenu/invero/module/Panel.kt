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
     * The windows to which this panel applied
     *
     * @attention do not abuse this property
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
    fun getSlotsMap(window: Window) = getSlotsMap(window.type.width)

    /**
     * Generate slotsmap for a certain window width
     */
    private fun getSlotsMap(windowWidth: Int): MappedSlots {
        return slotsMap.computeIfAbsent(windowWidth) {
            val result = mutableMapOf<Int, Int>()
            var counter = 0
            var baseLine = 0
            var baseIndex = pos
            while (baseIndex >= windowWidth) baseIndex -= windowWidth.also { baseLine++ }
            for (x in baseLine until baseLine + scale.second)
                for (y in baseIndex until baseIndex + scale.first)
                    result[if (y >= windowWidth) -1 else windowWidth * x + y] = counter++

            MappedSlots(result)
        }
    }

    fun getClaimedSlots(window: Window) = getSlotsMap(window).claimedSlots

    fun unregisterWindow(window: Window) = windows.remove(window)

    fun registerWindow(window: Window) = windows.add(window)

    fun forWindows(function: Window.() -> Unit) = windows.forEach(function)

    /**
     * Check if this element is current renderable
     */
    fun isRenderable(element: PanelElement): Boolean

    fun render(window: Window)

    fun renderAll() = forWindows { render(this) }

    fun handleClick(window: Window, e: InventoryClickEvent)

    fun handleItemsCollect(window: Window, e: InventoryClickEvent)

    fun handleItemsMove(window: Window, e: InventoryClickEvent)

}