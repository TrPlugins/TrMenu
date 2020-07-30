package me.arasple.mc.trmenu.listeners.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.modules.shortcut.Shortcuts
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

/**
 * @author Arasple
 * @date 2020/7/28 15:44
 */
@TListener
class ListenerInteractPlayer : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun rightClick(e: PlayerInteractEntityEvent) {
        val clicked = e.rightClicked
        if (clicked !is Player) return
        if (Shortcuts.rightClickPlayer(e.player, clicked)) {
            e.isCancelled = true
        }
    }

}