package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.element.Interactable
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BasePanel
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
open class StandardPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : BasePanel(scale, pos, weight) {

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true

        val relativeSlot = getSlotsMap(window).getRelative(e.slot)
        val element = elementsMap[relativeSlot]

        if (element is Interactable) element.passClickEvent(e)
    }

}