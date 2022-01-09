package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.console
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 11:38
 */
class ActionCommandConsole(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        {
            parseContentSplited(placeholderPlayer, ";").forEach {
                console().performCommand(it)
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

        override val name = "console".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionCommandConsole(value.toString(), option)
        }

    }

}