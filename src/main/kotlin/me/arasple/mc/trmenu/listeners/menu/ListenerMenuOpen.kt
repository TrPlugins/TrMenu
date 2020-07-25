package me.arasple.mc.trmenu.listeners.menu

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.api.factory.task.CloseTask
import me.arasple.mc.trmenu.data.MetaPlayer.setMeta
import me.arasple.mc.trmenu.data.MetaPlayer.updateInventoryContents
import me.arasple.mc.trmenu.data.Sessions.getMenuFactorySession
import me.arasple.mc.trmenu.modules.log.Log
import me.arasple.mc.trmenu.modules.log.Loger
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * @author Arasple
 * @date 2020/3/1 19:34
 */
@TListener
class ListenerMenuOpen : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onOpening(e: MenuOpenEvent) {
        val player = e.player

        Loger.log(Log.MENU_EVENT_OPEN, player.name, e.menu.id, e.page, e.reason.name)

        val factorySession = player.getMenuFactorySession()
        if (!factorySession.isNull()) {
            factorySession.menuFactory!!.closeTask?.run(CloseTask.Event(player, factorySession.menuFactory!!))
            factorySession.reset()
        }

        if (e.reason == MenuOpenEvent.Reason.SWITCH_PAGE) {
            return
        }
        player.setMeta("{reason}", e.reason.name).also {
            if (!e.menu.settings.events.openEvent.eval(player)) {
                e.isCancelled = true
                return
            }
        }
        player.updateInventoryContents()
    }

}