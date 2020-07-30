package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:53
 */
class ActionSetTitle : Action("set(-)?title") {

    override fun onExecute(player: Player) {
        Tasks.delay(3) {
            val session = getSession(player)
            if (!session.isNull()) {
                val menu = session.menu ?: return@delay
                val layout = session.layout ?: return@delay

                layout.displayInventory(player, getContent(player))
                menu.resetIcons(player, session)
            }
        }
    }

}