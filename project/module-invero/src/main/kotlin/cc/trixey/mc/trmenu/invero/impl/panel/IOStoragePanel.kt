package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BaseIOPanel
import cc.trixey.mc.trmenu.invero.module.base.BasePanel
import cc.trixey.mc.trmenu.invero.module.element.Interactable
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * @author Score2
 * @since 2022/11/15 18:30
 */
open class IOStoragePanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : BaseIOPanel(scale, pos, weight) {


    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
    }

    override fun handleDrag(window: Window, e: InventoryDragEvent) {
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        val relativeSlot = getSlotsMap(window).getRelative(e.slot)
        val element = elementsMap[relativeSlot]

        if (element is Interactable) element.passClickEvent(e)
    }

}