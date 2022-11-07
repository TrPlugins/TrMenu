package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.BasePanel
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
open class StandardPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
) : BasePanel(scale, pos, weight) {

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        getSlotsMap(window).getRelative(e.slot).let { relativeSlot ->
            getElement(relativeSlot)?.handleClick(e)
        }
    }

}