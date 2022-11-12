package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/6 16:51
 */
abstract class BasePagedPanel(
    scale: Pair<Int, Int>, pos: Int, weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    /**
     * Current page index
     */
    abstract var pageIndex: Int

    /**
     * Current max page index
     */
    val maxPageIndex: Int
        get() = pages.size - 1

    /**
     * Pages of elements
     */
    internal val pages = LinkedList<MappedElements>()

    /**
     * @see PanelInstance.slotsOccupied
     */
    override val slotsOccupied: Set<Int>
        get() = slotsOccupied()

    /**
     * @see PanelInstance.slotsUnoccupied
     */
    override val slotsUnoccupied: List<Int>
        get() = slotsUnoccupied()

    /**
     * Get the occupied slots of a certain page
     */
    fun slotsOccupied(index: Int = pageIndex) = this[index].slotsOccupied

    /**
     * Get the unoccupied slots of a certain page
     */
    fun slotsUnoccupied(index: Int = pageIndex) = slots - slotsOccupied(index)

    /**
     * Get a certain page layer
     */
    operator fun get(index: Int) = pages[index]

    /**
     * Get a certain page layer with default pageIndex
     */
    fun getPage(index: Int = pageIndex) = this[index]

    /**
     * Add a page to this panel
     *
     * @return last index of pages
     */
    fun addPage(page: MappedElements): Int {
        pages += page
        return pages.size - 1
    }

    /**
     * Switch to the next page
     */
    abstract fun nextPage(): Int

    /**
     * Switch to the previous page
     */
    abstract fun previousPage(): Int

    /**
     * Switch to a certain page
     */
    abstract fun switchPage(page: Int)

    /**
     * Check if this element is current renderable
     */
    override fun isRenderable(element: PanelElement): Boolean {
        return getPage().hasElement(element)
    }

    /**
     * Default render logic for BasePagedPanel
     */
    override fun render(window: Window) {
        getPage().forAbsoluteElements {
            it.render(window)
        }
        getPage().forDynamicElements {
            it.render(window)
        }
    }

}