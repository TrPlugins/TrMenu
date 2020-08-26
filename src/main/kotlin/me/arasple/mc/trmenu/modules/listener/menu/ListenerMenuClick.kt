package me.arasple.mc.trmenu.modules.listener.menu

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.event.MenuClickEvent
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import java.util.*


/**
 * @author Arasple
 * @date 2020/3/1 21:23
 */
@TListener
class ListenerMenuClick : Listener {

    @Suppress("DEPRECATION")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onClick(e: MenuClickEvent) {
        Mirror.eval("Menu:onClick(async)") {
            val player = e.player
            val menu = e.menu

            if (e.icon != null && !isInClickCooldown(player, menu)) {
                val icon = e.icon
                icon.getIconProperty(player).clickHandler.onClick(player, e.clickType)
            }
        }
    }

    companion object {

        private val CLICK_DELAY = mutableMapOf<UUID, Long>()

        @Deprecated("Require new method for click delay.")
        private fun isInClickCooldown(player: Player, menu: Menu): Boolean {
            val clickDelay = menu.settings.options.minClickDelay
            if (clickDelay <= 0) return false
            val expired = CLICK_DELAY.computeIfAbsent(player.uniqueId) { 0 }
            val left = expired - System.currentTimeMillis()

            return if (left > 0) {
                true
            } else {
                CLICK_DELAY[player.uniqueId] = System.currentTimeMillis() + clickDelay
                false
            }
        }

    }

}