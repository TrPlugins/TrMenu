package me.arasple.mc.trmenu.listeners.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.modules.shortcut.Shortcuts
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
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
        val player = (e.player as Player)
        val session = player.getMenuSession()

        if (!session.isNull()) {
            session.set(null, null, -1)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onClick(e: InventoryClickEvent) {
        if (Utils.isEventIgnoreCancelled(e)) return

        val player = e.whoClicked as Player

        if (e.clickedInventory == player.inventory && player.openInventory.topInventory == player.inventory) {
            if (e.click == ClickType.WINDOW_BORDER_LEFT) {
                Shortcuts.borderLeft(player)
            } else if (e.click == ClickType.WINDOW_BORDER_RIGHT) {
                Shortcuts.borderRight(player)
            }
        }
    }

}