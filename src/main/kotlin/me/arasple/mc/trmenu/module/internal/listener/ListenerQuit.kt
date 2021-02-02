package me.arasple.mc.trmenu.module.internal.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Arasple
 * @date 2021/1/27 12:14
 */
@TListener
class ListenerQuit : Listener {

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.player
        MenuSession.getSession(player).shut()
        MenuSession.removeSession(player)
        ReceptacleAPI.MANAGER.setViewingReceptacle(player, null)
    }

}