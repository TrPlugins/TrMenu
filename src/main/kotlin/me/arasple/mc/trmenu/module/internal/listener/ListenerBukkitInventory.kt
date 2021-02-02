package me.arasple.mc.trmenu.module.internal.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent

/**
 * @author Arasple
 * @date 2020/7/30 11:38
 * Fix the Bukkit inventory GUI incompatibility with other plugins
 */
@TListener
class ListenerBukkitInventory : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onOpen(e: InventoryOpenEvent) {
        val player = e.player as Player

        MenuSession.getSession(player).shut()
    }

}