package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BaseIOPanel
import cc.trixey.mc.trmenu.invero.module.base.BasePanel
import cc.trixey.mc.trmenu.invero.module.element.Interactable
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent

/**
 * @author Score2
 * @since 2022/11/15 18:30
 */
open class IOStoragePanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : BaseIOPanel(scale, pos, weight) {

    private var handlerDrag: (InventoryDragEvent) -> Unit = {}
    private var handlerMove: (InventoryClickEvent) -> Unit = {}

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        handlerMove(e)
    }

    override fun handleDrag(window: Window, e: InventoryDragEvent) {
        handlerDrag(e)
    }

    // TODO
    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        super.handleItemsCollect(window, e)
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        val relativeSlot = getSlotsMap(window).getRelative(e.rawSlot)
        val element = elementsMap[relativeSlot]

        if (element is Interactable) element.passClickEvent(e)
    }

    fun onDrag(event: (InventoryDragEvent) -> Unit) {
        handlerDrag = event
    }

    fun onMove(event: (InventoryClickEvent) -> Unit) {
        handlerMove = event
    }

}