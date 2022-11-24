package cc.trixey.mc.invero.common.window.event

import org.bukkit.event.inventory.*

/**
 * @author Arasple
 * @since 2022/11/22 14:57
 */
@JvmInline
value class BukkitInvEvent(val event: InventoryEvent) : InvEvent {

    override fun getType(): EventType {
        return when (event) {
            is InventoryOpenEvent -> EventType.INVENTORY_OPEN
            is InventoryCloseEvent -> EventType.INVENTORY_CLOSE
            is InventoryDragEvent -> EventType.ITEMS_DRAG
            is InventoryClickEvent -> {
                when (event.action) {
                    InventoryAction.MOVE_TO_OTHER_INVENTORY -> EventType.ITEMS_MOVE
                    InventoryAction.COLLECT_TO_CURSOR -> EventType.ITEMS_COLLECT
                    else -> EventType.CLICK
                }
            }

            else -> error("Unregonized event")
        }
    }

}