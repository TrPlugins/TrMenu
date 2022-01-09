package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/29 21:22
 */
class ActionReturn : AbstractAction() {

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        TODO("Not yet implemented")
    }

    companion object : ActionDesc {

        override val name = "return|break".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { _, _ -> ActionReturn() }

    }

}