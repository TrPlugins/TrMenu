package cc.trixey.mc.dinvero.bukkit

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @since 2022/10/23
 */
object BukkitInventoryEvents {

    @SubscribeEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    fun e(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        val holder = e.inventory.holder

        if (holder is BukkitInveroHolder) {
//            InveroInteractEvent(player, holder.invero, interactType, slot, clickedItem).apply {
//                call()
//                interactCallback.forEach { it(this) }
//                if (isCancelled) {
//                    player.sendCancelCoursor()
//                    refreshItem(slot)
//                }
//            }
        }
    }

}