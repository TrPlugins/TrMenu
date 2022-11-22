package cc.trixey.mc.invero.common.event

import org.bukkit.event.inventory.InventoryEvent

/**
 * @author Arasple
 * @since 2022/11/22 14:58
 */
interface InvEvent {

    fun getType(): EventType

    @Suppress("UNCHECKED_CAST")
    fun <T : InventoryEvent> toBukkit(): T {
        return (this as BukkitInvEvent).event as T
    }

}