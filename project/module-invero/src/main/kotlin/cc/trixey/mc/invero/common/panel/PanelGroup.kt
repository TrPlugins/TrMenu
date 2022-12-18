package cc.trixey.mc.invero.common.panel

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.util.findPanel
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * @author Arasple
 * @since 2022/11/22 18:40
 *
 * Paenl 集
 * 子 Paenl 的应用范围不得超过父级 （PanelGroup）的 scale/pos
 */
open class PanelGroup(scale: ScaleView, weight: PanelWeight) : PanelInstance(scale, weight), PanelContainer {

    override val panels: ArrayList<Panel> = ArrayList()

    override fun renderPanel() {
        panels.sortedByDescending { it.weight }.forEach { it.renderPanel() }
    }

    override fun getRenderability(element: Element): Set<Int> {
        error("PanelGroup itself does not support getRenderability()")
    }

    override fun renderElement(window: Window, element: Element) {
        error("PanelGroup itself does not support renderElement()")
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        findPanel(e.rawSlot)?.handleClick(window, e)
    }

    override fun handleDrag(window: Window, e: InventoryDragEvent) {
        findPanel(e.rawSlots.random())?.handleDrag(window, e)
    }

    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        findPanel(e.rawSlot)?.handleItemsCollect(window, e)
    }

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        findPanel(e.rawSlot)?.handleItemsMove(window, e)
    }

}