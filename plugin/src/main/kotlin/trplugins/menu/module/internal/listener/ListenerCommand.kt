package trplugins.menu.module.internal.listener

import trplugins.menu.api.event.MenuOpenEvent
import trplugins.menu.module.display.Menu
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent


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