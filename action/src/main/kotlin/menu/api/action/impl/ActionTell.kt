package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import taboolib.common.platform.ProxyPlayer
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/29 18:01
 */
class ActionTell(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        parseContentSplited(placeholderPlayer).forEach {
            player.sendMessage(it)
        }
    }

    companion object : ActionDesc {

        override val name = "tell|message|msg|talk".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionTell(value.toString(), option)
        }

    }

}