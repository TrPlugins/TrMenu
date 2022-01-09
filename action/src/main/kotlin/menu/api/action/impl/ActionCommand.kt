package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 11:38
 */
class ActionCommand(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        {
            parseContentSplited(placeholderPlayer, ";").forEach {
                player.performCommand(it)
            }
        }.also {
            if (isPrimaryThread) {
                it.invoke()
            } else {
                submit(async = false) {
                    it.invoke()
                }
            }
        }
    }

    companion object : ActionDesc {

        override val name = "command|cmd|player|execute".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionCommand(value.toString(), option)
        }

    }

}