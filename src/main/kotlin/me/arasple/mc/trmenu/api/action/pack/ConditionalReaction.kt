package me.arasple.mc.trmenu.api.action.pack

import me.arasple.mc.trmenu.module.internal.script.Condition
import org.bukkit.entity.Player

// Almost same has old Reactions.React class
class ConditionalReaction(priority: Int,
                          private val condition: String,
                          private val accept: Reactions,
                          private val deny: Reactions
                          ) : Reaction(priority) {

    private val hasCondition: Boolean = condition.isNotBlank()

    override fun isEmpty(): Boolean {
        return accept.isEmpty() && deny.isEmpty()
    }

    override fun react(player: Player): Boolean {
        return if (evalCondition(player)) accept.eval(player)
        else deny.eval(player)
    }

    private fun evalCondition(player: Player): Boolean {
        return if (hasCondition) Condition.eval(player, condition).asBoolean()
        else true
    }
}