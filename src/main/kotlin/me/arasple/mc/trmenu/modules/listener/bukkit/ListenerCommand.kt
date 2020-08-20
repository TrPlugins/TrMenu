package me.arasple.mc.trmenu.modules.listener.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.Extends.setArguments
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.util.Utils
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
                val matches = menu.settings.bindings.matchCommand(menu, message)
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