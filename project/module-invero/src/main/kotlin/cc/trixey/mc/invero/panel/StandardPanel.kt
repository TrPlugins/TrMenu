package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.base.BasePanel
import cc.trixey.mc.invero.common.base.Interactable
import cc.trixey.mc.invero.util.distinguishMark
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
class StandardPanel(
    scale: PanelScale,
    pos: Int,
    weight: PanelWeight
) : BasePanel(scale, pos, weight) {

    override fun renderElement(window: Window, element: Element) {
        if (!isRenderable(element)) return

        val slotMap = getSlotsMap(window)
        if (element is ItemProvider) {
            val itemStack = element.get()
            getElementsMap().find(element).forEach {
                val slot = slotMap.getAbsolute(it)
                window.inventorySet[slot] = itemStack.distinguishMark(slot)
            }
        }
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true

        val relativeSlot = getSlotsMap(window).getRelative(e.rawSlot)
        val element = getElementsMap()[relativeSlot]

        if (element is Interactable) element.passClickEvent(e)
    }

}