package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.util.Bungees
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 11:41
 */
class ActionConnect(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Bungees.connect(player, parseContent(placeholderPlayer))
    }

    companion object {

        private val name = "bungee|server|connect".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionConnect(value.toString(), option)
        }

        val registery = name to parser

    }

}