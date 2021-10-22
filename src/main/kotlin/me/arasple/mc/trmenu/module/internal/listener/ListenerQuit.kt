package me.arasple.mc.trmenu.module.internal.listener

import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.ui.receptacle.setViewingReceptacle

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
        Metadata.database.push(player)
        Menu.menus.forEach {
            it.icons.forEach { icon -> icon.onReset(session) }
        }
        MenuSession.removeSession(player)
        player.setViewingReceptacle(null)
    }

}