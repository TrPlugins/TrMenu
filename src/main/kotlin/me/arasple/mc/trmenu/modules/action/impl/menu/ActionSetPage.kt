package me.arasple.mc.trmenu.modules.action.impl.menu

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player
import kotlin.math.max

/**
 * @author Arasple
 * @date 2020/3/8 21:35
 */
class ActionSetPage : Action("((set|switch)?(-)?(shape|page))|shape|page") {

    override fun onExecute(player: Player) {
        val session = MenuSession.session(player)
        if (!session.isNull()) {
            session.menu?.open(player, max(NumberUtils.toInt(getContent(player), 0), session.menu?.layout?.layouts?.size ?: 0), MenuOpenEvent.Reason.PLAYER_COMMAND)
        }
    }

}