package me.arasple.mc.trmenu.module.internal.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.module.display.Menu
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
        val player = e.player
        val message = e.message.removePrefix("/")

        if (message.isNotBlank()) {
            Menu.menus.forEach {
                val matches = it.settings.matchCommand(it, message)
                if (matches != null) {
                    it.open(player) { session -> session.arguments = matches }
                    e.isCancelled = true
                    return
                }
            }
        }
    }


}