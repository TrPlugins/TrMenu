package me.arasple.mc.trmenu.modules.listener.menu

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.Extends.getMenuFactorySession
import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.api.Extends.setMeta
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.api.factory.task.CloseTask
import me.arasple.mc.trmenu.modules.data.Metas
import me.arasple.mc.trmenu.modules.service.log.Log
import me.arasple.mc.trmenu.modules.service.log.Loger
import me.arasple.mc.trmenu.modules.service.metrics.MetricsHandler
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
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
        Mirror.eval("Menu:preOpenEvent(async)") {
            MetricsHandler.increase(0)

            val player = e.player
            val menu = e.menu
            val page = e.page
            val reason = e.reason
            val session = player.getMenuFactorySession()

            Loger.log(menu, Log.EVENT_MENU_OPEN, player.name, page, reason.name)

            if (!session.isNull()) {
                session.menuFactory!!.closeTask?.run(CloseTask.Event(player, session, session.menuFactory!!))
                session.reset()
                return@eval
            }

            val expansions = menu.settings.options.expansions()
            if (expansions.isNotEmpty()) {
                player.sendLocale("MENU.DEPEND-EXPANSIONS", expansions.size)
                expansions.forEach { player.sendLocale("MENU.DEPEND-EXPANSIONS-FORMAT", it) }
                e.isCancelled = true
                return@eval
            }

            player.setMeta("{reason}", reason.name).also {
                if (!menu.settings.events.openEvent.eval(player)) {
                    e.isCancelled = true
                    return@eval
                }
            }
            Metas.updateInventoryContents(player)
        }
    }

}