package cc.trixey.mc.invero.common.window

import cc.trixey.mc.invero.common.window.event.EventType
import cc.trixey.mc.invero.common.window.event.InvEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/22 15:08
 */
abstract class BaseWindowBukkit(viewer: UUID) : BaseWindow(viewer) {

    abstract fun handleClick(e: InventoryClickEvent)

    abstract fun handleDrag(e: InventoryDragEvent)

    abstract fun handleItemsMove(e: InventoryClickEvent)

    abstract fun handleItemsCollect(e: InventoryClickEvent)

    abstract fun handleOpen(e: InventoryOpenEvent)

    abstract fun handleClose(e: InventoryCloseEvent)

    override fun handleEvent(e: InvEvent) {
        when (e.getType()) {
            EventType.INVENTORY_OPEN -> handleOpen(e.toBukkit())
            EventType.INVENTORY_CLOSE -> handleClose(e.toBukkit())
            EventType.ITEMS_DRAG -> handleDrag(e.toBukkit())
            EventType.ITEMS_MOVE -> handleItemsMove(e.toBukkit())
            EventType.ITEMS_COLLECT -> handleItemsCollect(e.toBukkit())
            EventType.CLICK -> handleClick(e.toBukkit())
        }
    }

}