package me.arasple.mc.trmenu.api.action.impl.menu

import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.api.event.MenuCloseEvent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/27 20:19
 */
class ActionClose : Action("close") {

    override fun onExecute(player: Player) {
        val session = getSession(player)
        if (!session.isNull()) {
            session.menu?.close(player, session.page, MenuCloseEvent.Reason.PLAYER, true, silent = false)
            player.updateInventory()
        }
    }

}