package me.arasple.mc.trmenu.module.internal.listener

import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.display.Menu
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import taboolib.common.platform.EventPriority
import taboolib.common.platform.SubscribeEvent


/**
 * @author Arasple
 * @date 2020/2/29 17:43
 */
object ListenerCommand {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val player = e.player
        val message = e.message.removePrefix("/")

        if (message.isNotBlank()) {
            Menu.menus.forEach {
                val matches = it.settings.matchCommand(it, message)
                if (matches != null) {
                    it.open(player, reason = MenuOpenEvent.Reason.BINDING_COMMANDS) { session ->
                        session.arguments = matches.toTypedArray()
                    }
                    e.isCancelled = true
                    return
                }
            }
        }
    }


}