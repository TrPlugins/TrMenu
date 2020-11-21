package me.arasple.mc.trmenu.api.action.impl.menu

import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.api.event.MenuCloseEvent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:20
 * Close menu without check conditions or executing close-actions
 */
class ActionSilentClose : Action("(force|silent)(-)?close") {

    override fun onExecute(player: Player) {
        val session = getSession(player)
        if (!session.isNull()) {
            session.menu?.close(player, session.page, MenuCloseEvent.Reason.PLAYER, closeInventory = false, silent = true)
        }
    }

}