package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Rubenicos
 * @date 2022/1/1 11:40
 */
class ActionSetAgent(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val agent = Bukkit.getPlayerExact(parseContent(placeholderPlayer)) ?: return

        player.session().agent = agent
    }

    companion object {

        private val name = "(set|change)-?agent".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetAgent(value.toString(), option)
        }

        val registery = name to parser

    }
}