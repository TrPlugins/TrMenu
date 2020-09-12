package me.arasple.mc.trmenu.api.action.impl.menu

import me.arasple.mc.trmenu.api.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:31
 */
class ActionRefresh : Action("(icon)?(-)?(refresh|update)") {

    override fun onExecute(player: Player) {
        val session = getSession(player)
        if (!session.isNull()) {
            session.menu?.icons?.forEach {
                it.resetCache(player.uniqueId)
                it.refreshIcon(player)
                it.displayItemStack(player)
            }
        }
    }

}