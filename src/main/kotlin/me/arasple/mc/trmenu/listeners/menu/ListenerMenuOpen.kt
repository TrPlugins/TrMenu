package me.arasple.mc.trmenu.listeners.menu

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.data.MetaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * @author Arasple
 * @date 2020/3/1 19:34
 */
@TListener
class ListenerMenuOpen : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onOpening(e: MenuOpenEvent) {
        val player = e.player

        MetaPlayer.setMeta(player, "{reason}", e.reason.name).also {
            if (!e.menu.settings.events.openEvent.react(player)) {
                e.isCancelled = true
                return
            }
        }
        MetaPlayer.updateInventoryContents(player)
    }

}