package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 22:15
 */
class ActionChat(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(placeholderPlayer).forEach {
            player.chat(it)
        }
    }

    companion object {

        private val name = "chat|send|say".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionChat(value.toString(), option)
        }

        val registery = name to parser

    }

}