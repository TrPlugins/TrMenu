package me.arasple.mc.trmenu.listeners.menu

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.events.MenuCloseEvent
import me.arasple.mc.trmenu.data.MetaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * @author Arasple
 * @date 2020/3/1 19:31
 */
@TListener
class ListenerMenuClose : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onClosing(e: MenuCloseEvent) {
        if (e.reason == MenuCloseEvent.Reason.SWITCH_PAGE) return
        if (!e.silent) e.menu.settings.events.closeEvent.eval(e.player)
        MetaPlayer.resetCache(e.player)
    }

}