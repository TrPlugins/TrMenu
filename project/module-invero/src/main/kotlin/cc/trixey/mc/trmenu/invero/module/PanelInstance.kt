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

    open var parent: PanelInstance? = null
    open val children: LinkedList<PanelInstance>? = null

    override val windows: LinkedList<Window> = LinkedList()
        get() {
            getParent()?.let {
                if (it is PanelInstance) return it.windows
            }
            return field
        }

    override var scale = scale
        set(value) {
            field = value
            slotsMap.clear()
        }

    override var weight = weight
        set(value) {
            field = value
            forWindows { renderWindow(true) }
        }

    final override var pos = pos
        private set


    override val slots by lazy { (0 until scale.first * scale.second).toList() }


    override val slotsMap = LinkedHashMap<Int, MappedSlots>()

    /**
     * The relative slots which have already been taken by elements
     */
    abstract val slotsOccupied: Set<Int>

    /**
     * The relative slots that are currently available
     */
    abstract val slotsUnoccupied: List<Int>

    override fun getChildren() = children?.map { it }

    override fun getParent() = parent as Parentable?

    override fun getSlotsMap(parent: Parentable): MappedSlots {
        val width = when (parent) {
            is Window -> parent.type.width
            is PanelInstance -> parent.scale.first
            else -> error("?")
        }
        return slotsMap.computeIfAbsent(width) {
            MappedSlots.from(scale, pos, width)
        }
    }

    override fun isRenderable(element: PanelElement): Boolean {
        return true
    }

    override fun unregisterWindow(window: Window) {
        windows.remove(window)
    }

    override fun registerWindow(window: Window) {
        windows.add(window)
    }

    override fun forWindows(function: Window.() -> Unit) {
        windows.forEach(function)
    }

    override fun renderAll() = forWindows { renderPanel(this) }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    /**
     * Change the starting position of this panel
     */
    fun markPosition(mark: Int) {
        pos = mark
        slotsMap.clear()
    }

    /**
     * TODO
     * Still testing wipeAll function
     */
    fun wipePanel() {
        forWindows {
            getSlotsMap(this).claimedSlots.forEach { pairedInventory[it] = null }
        }
    }

}