package me.arasple.mc.trmenu.module.internal.listener

import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @date 2020/7/30 11:38
 * Fix the Bukkit inventory GUI incompatibility with other plugins
 */
object ListenerBukkitInventory {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onOpen(e: InventoryOpenEvent) {
        val player = e.player as Player

        MenuSession.getSession(player).close(closePacket = true, updateInventory = true)
    }

}