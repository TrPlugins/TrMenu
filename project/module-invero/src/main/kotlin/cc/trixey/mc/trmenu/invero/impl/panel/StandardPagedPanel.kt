package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.BasePagedPanel
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/6 16:30
 */
class StandardPagedPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
) : BasePagedPanel(scale, pos, weight) {

    override var pageIndex = 0
        set(value) {
            wipePanel()
            renderAll()
            field = value
        }

    override fun nextPage() {
        if (pageIndex < maxPageIndex) pageIndex++
    }

    override fun previousPage() {
        if (pageIndex > 0) pageIndex--
    }

    override fun switchPage(page: Int) {
        pageIndex = page
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        getSlotsMap(window).getRelative(e.slot).let { relativeSlot ->
            getPage().getElement(relativeSlot)?.handleClick(e)
        }
    }

    override fun render(window: Window) {
        getPage().forEach { _, panelElement ->
            panelElement.render(window)
        }
    }

}