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
class StandardPagedPanel(scale: Pair<Int, Int>, pos: Int, weight: PanelWeight) : BasePagedPanel(scale, pos, weight) {

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

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        val relativeslot = getSlotsMap(window).getRelative(e.slot)
        val element = getPage().getAbsoluteElement(relativeslot)

        element?.passClickEvent(e)
    }

}