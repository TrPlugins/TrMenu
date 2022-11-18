package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.base.BaseIOPanel
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

    private var handlerClick: (InventoryClickEvent) -> Unit = {}
    private var handlerDrag: (InventoryDragEvent) -> Unit = {}
    private var handlerMove: (InventoryClickEvent) -> Unit = {}

    override fun renderPanel(window: Window) {
        storage.forEach { (index, itemStack) ->
            val slot = getSlotsMap(window).getActual(index)
            window.inventorySet[slot] = itemStack
        }
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        handlerClick(e)
        updateStorage(window)
    }

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        handlerMove(e)
        updateStorage(window)
    }

    override fun handleDrag(window: Window, e: InventoryDragEvent) {
        handlerDrag(e)
        updateStorage(window)
    }

    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        super.handleItemsCollect(window, e)
    }

    fun onClick(event: (InventoryClickEvent) -> Unit) {
        handlerClick = event
    }

    fun onDrag(event: (InventoryDragEvent) -> Unit) {
        handlerDrag = event
    }

    fun onMove(event: (InventoryClickEvent) -> Unit) {
        handlerMove = event
    }

}