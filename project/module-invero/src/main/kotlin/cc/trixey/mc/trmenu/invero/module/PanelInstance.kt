package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import cc.trixey.mc.trmenu.invero.module.`object`.MappedSlots
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/7 22:18
 */
abstract class PanelInstance(scale: Pair<Int, Int>, pos: Int, weight: PanelWeight) : Panel {

    /**
     * Scale of this panel
     * Consists of width and height
     */
    override var scale = scale
        set(value) {
            field = value
            slotsMap.clear()
        }

    /**
     * The weight of this panel
     */
    override var weight = weight
        set(value) {
            field = value
            forWindows { renderWindow(true) }
        }

    /**
     * Layout position mark
     * Starting index of real slot
     */
    final override var pos = pos
        private set

    /**
     * Slots this panel have
     * (Relative slots, starting from 0)
     */
    override val slots by lazy { (0 until scale.first * scale.second).toList() }

    /**
     * Map of relative slots and actual slots
     * The key (int) is the width of a Window
     */
    override val slotsMap = LinkedHashMap<Int, MappedSlots>()

    /**
     * The relative slots which have already been taken by elements
     */
    abstract val slotsOccupied: Set<Int>

    /**
     * The relative slots that are currently available
     */
    abstract val slotsUnoccupied: List<Int>

    /**
     * Windows that are using this panel
     */
    override val windows = LinkedList<Window>()

    /**
     * Get sub-panels
     */
    override fun getChildren() = null

    /**
     * Get parent panel
     */
    override fun getParent() = null

    /**
     * Change the starting position of this panel
     */
    fun markPosition(mark: Int) {
        pos = mark
        slotsMap.clear()
    }

    override fun isRenderable(element: PanelElement): Boolean {
        return true
    }

    /**
     * TODO
     * Still testing wipeAll function
     */
    fun wipePanel() {
        forWindows {
            getClaimedSlots(this).forEach { pairedInventory[it] = null }
        }
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

}