package me.arasple.mc.trmenu.api.action.impl.menu

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.Extends.setArguments
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.util.Tasks
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
            val arguments = if (args.size == 1) arrayOf() else args.toTypedArray().copyOfRange(1, args.size)
            val menu = TrMenuAPI.getMenuById(split[0]) ?: return@delay
            val page = if (split.size > 1) NumberUtils.toInt(split[1], -1) else -1

            player.setArguments(arguments)
            menu.open(player, page, MenuOpenEvent.Reason.PLAYER_COMMAND)
        }
    }

}