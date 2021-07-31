package me.arasple.mc.trmenu.module.internal.listener

import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.SubscribeEvent

/**
 * @author Arasple
 * @date 2021/1/27 12:14
 */
object ListenerQuit {

    @SubscribeEvent
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.player
        val session = MenuSession.getSession(player)
        session.shut()
        Menu.menus.forEach {
            it.icons.forEach { icon -> icon.onReset(session) }
        }
        MenuSession.removeSession(player)
        ReceptacleAPI.MANAGER.setViewingReceptacle(player, null)
    }

}