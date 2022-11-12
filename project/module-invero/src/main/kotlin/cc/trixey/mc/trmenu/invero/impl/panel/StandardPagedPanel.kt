package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BasePagedPanel
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit

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
            if (value in 0..maxPageIndex) field = value
            submit {
                wipePanel()
                renderAll()
            }
        }

    override fun nextPage(): Int = pageIndex++

    override fun previousPage(): Int = pageIndex--

    override fun switchPage(page: Int) {
        pageIndex = page
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        getSlotsMap(window).getRelative(e.slot).let { getPage().getElement(it)?.handleEvent(e) }
    }

    override fun render(window: Window) {
        getPage().forEach { _, panelElement ->
            panelElement.render(window)
        }
    }

}