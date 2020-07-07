package me.arasple.mc.trmenu.listeners.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.data.MetaPlayer
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Arasple
 * @date 2020/3/19 11:52
 */
@TListener
class ListenerPlayerQuit : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.player
        Tasks.runTask(Runnable {
            MetaPlayer.resetCache(player)
            MenuSession.remove(player)
        }, true)
    }

}