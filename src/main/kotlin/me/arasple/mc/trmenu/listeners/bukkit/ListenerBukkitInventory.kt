package me.arasple.mc.trmenu.listeners.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.modules.shortcut.Shortcuts
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
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
        if (Utils.isEventIgnoreCancelled(e)) return
        val player = e.player as Player

        player.getMenuSession().safeClose(player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onClick(e: InventoryClickEvent) {
        if (Utils.isEventIgnoreCancelled(e)) return

        val player = e.whoClicked as Player

        if (player.openInventory.topInventory.holder == player.inventory.holder && e.slot < 0) {
            Shortcuts.borderClick(player, e.click)
        }
    }

}