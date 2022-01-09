package trplugins.menu.api.reaction

import trplugins.menu.api.action.base.AbstractAction
import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.Actions.Companion.scriptParser

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
class ConditionalReaction(priority: Int,
                          private val condition: String,
                          private val accept: Reactions,
                          private val deny: Reactions
                          ) : Reaction(priority) {

    private val hasCondition: Boolean = condition.isNotBlank()

    override fun isEmpty(): Boolean {
        return accept.isEmpty() && deny.isEmpty()
    }

    override fun getActions(player: ProxyPlayer): List<AbstractAction> {
        return if (evalCondition(player)) accept.getActions(player)
        else deny.getActions(player)
    }

    fun evalCondition(player: ProxyPlayer): Boolean {
        return if (hasCondition) scriptParser.parse(player, condition).asBoolean()
        else true
    }
}