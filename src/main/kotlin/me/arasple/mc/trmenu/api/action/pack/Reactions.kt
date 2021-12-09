package me.arasple.mc.trmenu.api.action.pack

import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 17:51
 */
data class Reactions(private val reacts: List<Reaction>) {

    companion object {

        fun ofReaction(any: Any?): Reactions {
            val reacts = mutableListOf<Reaction>()
            any ?: return Reactions(reacts)
            if (any is List<*>) {
                var order = 0
                any.filterNotNull().forEach { Reaction.of(order++, it)?.let { react -> reacts.add(react) } }
            } else Reaction.of(-1, any)?.let { reacts.add(it) }

            return Reactions(reacts)
        }

    }

    fun eval(session: MenuSession): Boolean {
        return eval(session.viewer)
    }

    fun eval(player: Player): Boolean {
        if (isEmpty()) return true

        return Actions.runAction(player, getActions(player))
    }

    fun getActions(player: Player): List<AbstractAction> {
        return mutableListOf<AbstractAction>().run {
            reacts.sortedBy { it.priority }.forEach { addAll(it.getActions(player)) }
            this
        }
    }

    fun isEmpty(): Boolean {
        return reacts.isEmpty() || reacts.all { it.isEmpty() }
    }

}