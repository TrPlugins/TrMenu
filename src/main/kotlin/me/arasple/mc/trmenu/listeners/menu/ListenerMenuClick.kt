package me.arasple.mc.trmenu.listeners.menu

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.events.MenuClickEvent
import me.arasple.mc.trmenu.display.Menu
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onClick(e: MenuClickEvent) {
        val player = e.player
        val menu = e.menu

        if (e.icon != null && !isInClickCooldown(player, menu)) {
            val icon = e.icon
            icon.getIconProperty(player).clickHandler.onClick(player, e.clickType)
        }
    }

    companion object {

        private val CLICK_DELAY = mutableMapOf<UUID, Long>()

        @Deprecated("Require new method for click delay.")
        private fun isInClickCooldown(player: Player, menu: Menu): Boolean {
            val clickDelay = menu.settings.options.minClickDelay * 50
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