package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.api.events.MenuCloseEvent
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/27 20:19
 */
class ActionClose : Action("close") {

    override fun onExecute(player: Player) {
        val session = MenuSession.session(player)
        if (!session.isNull()) {
            session.menu?.close(player, session.page, MenuCloseEvent.Reason.PLAYER, true, silent = false)
        }
    }

}