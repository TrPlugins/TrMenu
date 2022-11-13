package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BasePagedPanel
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/6 16:30
 */
open class PagedStandardPanel(scale: Pair<Int, Int>, pos: Int, weight: PanelWeight) : BasePagedPanel(scale, pos, weight) {

    /**
     * @see BasePagedPanel.pageIndex
     */
    override var pageIndex = 0
        set(value) {
            if (value in 0..maxPageIndex) field = value
            submit {
                wipePanel()
                renderAll()
            }
        }

    /**
     * @see BasePagedPanel.maxPageIndex
     */
    override val maxPageIndex: Int
        get() = pagedElements.lastIndex

    /**
     * @see BasePagedPanel.pagedElements
     */
    val pagedElements = LinkedList<MappedElements>()

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
     * @see BasePagedPanel.nextPage
     */
    override fun nextPage(): Int = pageIndex++

    /**
     * @see BasePagedPanel.previousPage
     */
    override fun previousPage(): Int = pageIndex--

    /**
     * @see BasePagedPanel.switchPage
     */
    override fun switchPage(page: Int) {
        pageIndex = page
    }

    /**
     * Get a certain page layer with default pageIndex
     */
    open fun getPage(index: Int = pageIndex) = pagedElements[index]

    /**
     * Add a page to this panel
     *
     * @return last index of pages
     */
    fun addPage(page: MappedElements): Int {
        pagedElements += page
        return pagedElements.lastIndex
    }

    /**
     * Get the occupied slots of a certain page
     */
    fun slotsOccupied(index: Int = pageIndex) = pagedElements[index].slotsOccupied

    /**
     * Get the unoccupied slots of a certain page
     */
    fun slotsUnoccupied(index: Int = pageIndex) = slots - slotsOccupied(index)

    /**
     * Default render logic for BasePagedPanel
     */
    override fun renderPanel(window: Window) {
        getPage().forAbsoluteElements {
            it.renderElement(window)
        }
        getPage().forDynamicElements {
            it.renderElement(window)
        }
    }

    /**
     * Check if this element is current renderable
     */
    override fun isRenderable(element: PanelElement): Boolean {
        return getPage().hasElement(element)
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true

        val relativeslot = getSlotsMap(window).getRelative(e.slot)
        val element = getPage()[relativeslot]

        element?.passClickEvent(e)
    }

}