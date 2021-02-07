package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 16:33
 *
 * open: <MenuId>:<PageIndex> <Arguments>
 */
class ActionOpen(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val args = parseContentSplited(placeholderPlayer, " ")
        val split = args[0].split(":")
        val arguments = if (args.size == 1) arrayOf() else args.toTypedArray().copyOfRange(1, args.size)
        val menu = TrMenuAPI.getMenuById(split[0]) ?: return
        val page = split.getOrNull(1)?.toIntOrNull() ?: 0

        menu.open(player, page, MenuOpenEvent.Reason.PLAYER_COMMAND) {
            if (arguments.isNotEmpty()) it.arguments = arguments
        }
    }

    companion object {

        private val name = "opens?|(open)?-?gui|(tr)?menu".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionOpen(value.toString(), option)
        }

        val registery = name to parser

    }

}