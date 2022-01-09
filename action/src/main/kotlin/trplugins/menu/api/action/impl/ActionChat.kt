package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/29 22:15
 */
class ActionChat(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
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