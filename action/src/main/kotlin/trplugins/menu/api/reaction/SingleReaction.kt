package trplugins.menu.api.reaction

import trplugins.menu.api.action.base.AbstractAction
import taboolib.common.platform.ProxyPlayer

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
class SingleReaction(priority: Int, private val actions: List<AbstractAction>) : Reaction(priority) {

    override fun isEmpty(): Boolean {
        return actions.isEmpty()
    }

    override fun getActions(player: ProxyPlayer): List<AbstractAction> {
        return actions
    }
}