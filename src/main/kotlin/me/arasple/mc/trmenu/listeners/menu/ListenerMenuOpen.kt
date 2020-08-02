package me.arasple.mc.trmenu.listeners.menu

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.api.factory.task.CloseTask
import me.arasple.mc.trmenu.data.MetaPlayer.setMeta
import me.arasple.mc.trmenu.data.MetaPlayer.updateInventoryContents
import me.arasple.mc.trmenu.data.Sessions.getMenuFactorySession
import me.arasple.mc.trmenu.modules.log.Log
import me.arasple.mc.trmenu.modules.log.Loger
import me.arasple.mc.trmenu.modules.metrics.MetricsHandler
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
        MetricsHandler.increase(0)

        val player = e.player
        val menu = e.menu
        val page = e.page
        val reason = e.reason
        val session = player.getMenuFactorySession()

        Loger.log(Log.MENU_EVENT_OPEN, player.name, menu.id, page, reason.name)

        if (!session.isNull()) {
            session.menuFactory!!.closeTask?.run(CloseTask.Event(player, session, session.menuFactory!!))
            session.reset()
            return
        }

        if (reason == MenuOpenEvent.Reason.SWITCH_PAGE) return

        val expansions = menu.settings.options.expansions()
        if (expansions.isNotEmpty()) {
            TLocale.sendTo(player, "MENU.DEPEND-EXPANSIONS", expansions.size)
            expansions.forEach {
                TLocale.sendTo(player, "MENU.DEPEND-EXPANSIONS-FORMAT", it)
            }
            e.isCancelled = true
            return
        }

        player.setMeta("{reason}", reason.name).also {
            if (!menu.settings.events.openEvent.eval(player)) {
                player.sendMessage("Cancelled: ${menu.settings.events.openEvent.isEmpty}")
                e.isCancelled = true
                return
            }
        }
        player.updateInventoryContents()
    }

}