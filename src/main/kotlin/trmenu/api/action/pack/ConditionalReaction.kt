package trmenu.api.action.pack

import trmenu.api.action.base.AbstractAction
import trmenu.module.internal.script.Condition
import org.bukkit.entity.Player

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

    override fun getActions(player: Player): List<AbstractAction> {
        return if (evalCondition(player)) accept.getActions(player)
        else deny.getActions(player)
    }

    private fun evalCondition(player: Player): Boolean {
        return if (hasCondition) Condition.eval(player, condition).asBoolean()
        else true
    }
}