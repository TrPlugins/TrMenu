package cc.trixey.mc.invero.impl.container.panel

import cc.trixey.mc.invero.Element
import cc.trixey.mc.invero.PanelWeight
import cc.trixey.mc.invero.ScaleView
import cc.trixey.mc.invero.Window
import cc.trixey.mc.invero.panel.BaseIOPanel
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * @author Score2
 * @since 2022/11/15 18:30
 */
open class IOStoragePanel(scale: ScaleView, weight: PanelWeight) : BaseIOPanel(scale, weight) {

    private var handlerClick: (InventoryClickEvent) -> Unit = {}
    private var handlerDrag: (InventoryDragEvent) -> Unit = {}
    private var handlerMove: (InventoryClickEvent) -> Unit = {}

    override fun renderPanel() {
        forWindows {
            storage.forEach { (index, itemStack) ->
                index.toUpperSlot()?.let {
                    inventorySet[it] = itemStack
                }
            }
        }
    }

    override fun getRenderability(element: Element): Set<Int> {
        TODO("Not yet implemented")
    }

    override fun renderElement(window: Window, element: Element) {
        error("IO Panel supports no element")
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