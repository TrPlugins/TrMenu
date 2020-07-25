package me.arasple.mc.trmenu.modules.action.impl.menu

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.data.MetaPlayer.setArguments
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/29 21:36
 */
class ActionOpen : Action("open(s)?|(open)?(-)?gui|(tr)?menu") {

    override fun onExecute(player: Player) {
        Tasks.delay(1) {
            val args = getContentSplited(player, " ")
            val split = args[0].split(":")
            val menu = TrMenuAPI.getMenuById(split[0]) ?: return@delay
            val page = if (split.size > 1) NumberUtils.toInt(split[1], -1) else -1

            player.setArguments(if (args.size == 1) arrayOf() else args.toTypedArray().copyOfRange(1, args.size - 1))
            menu.open(player, page, MenuOpenEvent.Reason.PLAYER_COMMAND)
        }
    }

}