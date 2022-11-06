package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.*
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
class BasePanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL
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
    override var pos = pos
        set(value) {
            field = value
            slotsMap.clear()
        }

    /**
     * Slots this panel have
     * (Relative slots, starting from 0)
     */
    override val slots by lazy { (0 until scale.first * scale.second).toList() }

    /**
     * Claimed Slots Map
     *
     * Width: SlotMap(Actual:Relative)
     */
    private val slotsMap = LinkedHashMap<Int, SlotMap>()

    /**
     * Windows that are using this panel
     */
    override val windows = LinkedList<Window>()

    /**
     * Static-Slot Elements of this panel
     */
    override val elements = LinkedHashMap<Int, PanelElement>()

    /**
     * Dynamic-Slot Elements of this panel
     */
    override val dynamicElements = LinkedList<PanelElementDynamic>()

    /**
     * Handle click event that passed to this panel
     */
    override fun handleClick(window: Window, e: InventoryClickEvent) {
        getSlotsMap(window).getRelative(e.slot)?.let { relativeSlot ->
            elements[relativeSlot]?.handleClick(e)
        }
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
     * Get the slotsmap for a window
     */
    override fun getSlotsMap(window: Window) = getSlotsMap(window.type.width)

    /**
     * Generate slotsmap for a certain window width
     */
    private fun getSlotsMap(windowWidth: Int): SlotMap {
        return slotsMap.computeIfAbsent(windowWidth) {
            val result = mutableMapOf<Int, Int>()
            var counter = 0
            var baseLine = 0
            var baseIndex = pos
            while (baseIndex >= windowWidth) baseIndex -= windowWidth.also { baseLine++ }
            for (x in baseLine until baseLine + scale.second)
                for (y in baseIndex until baseIndex + scale.first)
                    result[if (y >= windowWidth) -1 else windowWidth * x + y] = counter++

            SlotMap(result)
        }
    }

    /**
     * No children panels supported
     */
    override fun getChildren() = null

    /**
     * TODO
     * Parent panel support
     */
    override fun getParent() = null

}