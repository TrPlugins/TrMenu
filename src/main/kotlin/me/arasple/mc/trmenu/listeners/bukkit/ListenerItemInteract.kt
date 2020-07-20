package me.arasple.mc.trmenu.listeners.bukkit

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.display.Menu
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * @author Arasple
 * @date 2020/3/17 22:00
 */
@TListener
class ListenerItemInteract : Listener {

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (Version.isAfter(Version.v1_9) && e.hand == EquipmentSlot.OFF_HAND) return

        val player = e.player
        val item = e.item ?: return

        val menu = Menu.getMenus().find { it.settings.bindings.matchItem(player, item) }
        if (menu != null) {
            e.isCancelled = true
            menu.open(player, -1, MenuOpenEvent.Reason.BINDING_ITEMS)
        }
    }

}