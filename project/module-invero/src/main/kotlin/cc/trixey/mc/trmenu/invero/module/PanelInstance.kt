package cc.trixey.mc.trmenu.invero.module

import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/7 22:18
 */
abstract class PanelInstance(
    scale: Pair<Int, Int>, pos: Int, weight: PanelWeight
) : Panel {

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
            forWindows { render(true) }
        }

    /**
     * Layout position mark
     * Starting index of real slot
     */
    final override var pos = pos
        private set

    /**
     * Change the starting position of this panel
     */
    fun markPosition(mark: Int) {
        pos = mark
        slotsMap.clear()
    }

    /**
     * Slots this panel have
     * (Relative slots, starting from 0)
     */
    override val slots by lazy { (0 until scale.first * scale.second).toList() }

    override val slotsMap = LinkedHashMap<Int, MappedSlots>()

    /**
     * Windows that are using this panel
     */
    override val windows = LinkedList<Window>()

    /**
     * Handle click event that passed to this panel
     */
    override fun handleClick(window: Window, e: InventoryClickEvent) {

    }

    /**
     * Handle items collect event that concerned this panel
     */
    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {

    }

    /**
     * Handle items move event that concerned this panel
     */
    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {

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

    /**
     * No children panels supported
     */
    override fun getChildren() = null

    /**
     * Parent panel support
     */
    override fun getParent() = null

}