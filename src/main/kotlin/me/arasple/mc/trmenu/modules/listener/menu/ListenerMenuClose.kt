package me.arasple.mc.trmenu.modules.listener.menu

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.api.Extends.resetCache
import me.arasple.mc.trmenu.api.event.MenuCloseEvent
import me.arasple.mc.trmenu.api.nms.NMS
import me.arasple.mc.trmenu.modules.service.log.Log
import me.arasple.mc.trmenu.modules.service.log.Loger
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
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
        Mirror.eval("Menu:onClose(async)") {
            val player = e.player
            val menu = e.menu

            Loger.log(menu, Log.EVENT_MENU_CLOSE, player.name, e.page, e.reason.name, e.silent)

            if (e.reason.isSwitch()) {
                NMS.sendClearNonIconSlots(player, player.getMenuSession())
                return@eval
            }

            menu.icons.forEach { icon ->
                icon.defIcon.display.item.cache.remove(player.uniqueId)
                icon.subIcons.forEach { sub ->
                    sub.display.item.cache.remove(player.uniqueId)
                }
                icon.currentIndex.remove(player.uniqueId)
            }
            menu.viewers.remove(player)

            if (!e.silent) menu.settings.events.closeEvent.eval(player)

            player.resetCache()
        }
    }

}