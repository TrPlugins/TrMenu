package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.MappedElements
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BasePagedPanel
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/6 16:30
 */
open class PagedStandardPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : BasePagedPanel(scale, pos, weight) {

    /**
     * @see BasePagedPanel.pageIndex
     */
    override var pageIndex = 0
        set(value) {
            var changed = false
            if (value in 0..maxPageIndex) field = value.also { changed = true }
            submit {
                if (changed) {
                    renderAll()
                    wipePanel(getUnoccupiedSlots(field))
                }
            }
        }

    /**
     * @see BasePagedPanel.maxPageIndex
     */
    override val maxPageIndex: Int
        get() = pagedElements.lastIndex

    /**
     * Elements of different page
     */
    internal val pagedElements = ArrayList<MappedElements>()

    /**
     * Default fall back elements for every page
     */
    internal val fallbackElements = MappedElements()

    /**
     * @see BasePagedPanel.getOccupiedSlots
     */
    override fun getOccupiedSlots(page: Int): Set<Int> {
        return getPage(page).occupiedSlots + fallbackElements.occupiedSlots
    }

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
     * Get fallback elements layout
     */
    open fun getFallbackElements(): MappedElements {
        return fallbackElements
    }

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
     * Default render logic for BasePagedPanel
     */
    override fun renderPanel(window: Window) {
        // TODO ? NOT RENDER DYNAMIC OF FALLBACK?
        getFallbackElements().forAbsoluteElements {
            it.renderElement(window)
        }
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
        return getPage().has(element) || getFallbackElements().has(element)
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true

        val slot = getSlotsMap(window).getRelative(e.rawSlot)
        val element = getPage()[slot] ?: getFallbackElements()[slot]

        element?.passClickEvent(e)
    }

}