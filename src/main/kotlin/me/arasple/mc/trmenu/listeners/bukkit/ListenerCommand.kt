package me.arasple.mc.trmenu.listeners.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.data.MetaPlayer.setArguments
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

/**
 * @author Arasple
 * @date 2020/2/29 17:43
 */
@TListener
class ListenerCommand : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (Utils.isEventIgnoreCancelled(e)) return

        val player = e.player
        val message = e.message.removePrefix("/")

        if (message.isNotEmpty()) {
            for (menu in Menu.getMenus()) {
                val matches = menu.settings.bindings.matchCommand(message)
                if (matches != null) {
                    player.setArguments(matches)
                    menu.open(player, -1, MenuOpenEvent.Reason.PLAYER_COMMAND)
                    e.isCancelled = true
                    break
                }
            }
        }
    }


}