package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:53
 */
class ActionSetTitle : Action("set(-)?title") {

    override fun onExecute(player: Player) {
        val session = getSession(player)
        if (!session.isNull()) {
            val layout = session.layout
            val icons = session.menu?.icons
            if (layout != null && icons != null) {
                layout.displayInventory(player, getContent(player))
                icons.forEach { it.displayItemStack(player) }
            }
        }
    }

}