package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BasePanel
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
open class StandardPanel(scale: Pair<Int, Int>, pos: Int, weight: PanelWeight) : BasePanel(scale, pos, weight) {

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        val relativeslot = getSlotsMap(window).getRelative(e.slot)
        val element = getAbsoluteElement(relativeslot)

        element?.passClickEvent(e)
    }

}