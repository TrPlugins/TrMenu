package trplugins.menu.api.action.impl

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.api.action.base.ActionOption

/**
 * @author Arasple
 * @date 2021/1/29 21:22
 */
class ActionDelay(content: String) : AbstractAction(content) {

    fun getDelay(player: ProxyPlayer) = parseContent(player).toLongOrNull() ?: 0L

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        TODO("Not yet implemented")
    }

    companion object : ActionDesc {

        override val name = "delay|wait".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, _ ->
            ActionDelay(value.toString())
        }

    }

}