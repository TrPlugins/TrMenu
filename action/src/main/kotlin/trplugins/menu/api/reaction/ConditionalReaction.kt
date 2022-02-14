package trplugins.menu.api.reaction

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionEntry

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
class ConditionalReaction(handle: ActionHandle,
                          priority: Int,
                          private val condition: String,
                          private val accept: Reactions,
                          private val deny: Reactions
                          ) : Reaction(handle, priority) {

    private val hasCondition: Boolean = condition.isNotBlank()

    override fun isEmpty(): Boolean {
        return accept.isEmpty() && deny.isEmpty()
    }

    override fun getActions(player: ProxyPlayer): List<ActionEntry> {
        return if (evalCondition(player)) accept.getActions(player)
        else deny.getActions(player)
    }

    fun evalCondition(player: ProxyPlayer): Boolean {
        return if (hasCondition) handle.conditionParser.apply(player, condition).asBoolean()
        else true
    }
}