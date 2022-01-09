package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import trmenu.api.action.base.ActionDesc

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

    companion object : ActionDesc {

        override val name = "chat|send|say".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionChat(value.toString(), option)
        }

    }

}