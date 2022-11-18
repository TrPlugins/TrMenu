package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BasePagedPanel
import cc.trixey.mc.trmenu.invero.module.base.BasePanel
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/12 22:12
 */
class PagedNetesedPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : BasePagedPanel(scale, pos, weight) {

    override val children: ArrayList<PanelInstance> = ArrayList()

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
        get() = children.lastIndex

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
    fun getPage(index: Int = pageIndex) = children[index]

    /**
     * Add a page to this panel
     *
     * @return last index of pages
     */
    fun addPage(page: PanelInstance): Int {
        children += page.also {
            it.parent = this
        }
        return children.lastIndex
    }

    /**
     * @see BasePagedPanel.getOccupiedSlots
     */
    override fun getOccupiedSlots(page: Int): Set<Int> {
        return when (val it = getPage(page)) {
            is BasePanel -> it.getOccupiedSlots()
            is BasePagedPanel -> it.getOccupiedSlots(it.pageIndex)
            else -> error("getSlotsOccupied")
        }
    }

    /**
     * Default render logic for BasePagedPanel
     */
    override fun renderPanel(window: Window) {
        getPage().renderPanel(window)
    }

    /**
     * Check if this element is current renderable
     */
    override fun isRenderable(element: PanelElement): Boolean {
        return getPage().isRenderable(element)
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        getPage().handleClick(window, e)
    }


}