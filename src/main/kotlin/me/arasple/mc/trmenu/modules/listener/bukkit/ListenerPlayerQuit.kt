package me.arasple.mc.trmenu.modules.listener.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.Extends.removeMenuSession
import me.arasple.mc.trmenu.api.Extends.resetCache
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.function.inputer.InputCatcher.removeCatcher
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
        
        player.removeCatcher()
        player.resetCache()
        player.removeMenuSession()

        Menu.getAllMenus().flatMap { it.value }.forEach {
            it.viewers.remove(player)
            it.tasking.reset(player)
        }
    }

}