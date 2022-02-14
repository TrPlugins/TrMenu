package trplugins.menu.api.reaction

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionEntry

/**
 * @author Arasple
 * @date 2021/1/29 17:51
 */
data class Reactions(val handle: ActionHandle, private val reacts: List<Reaction>) {

    fun eval(player: ProxyPlayer): Boolean {
        if (isEmpty()) return true

        return handle.runAction(player, getActions(player))
    }

    fun getActions(player: ProxyPlayer): List<ActionEntry> {
        return mutableListOf<ActionEntry>().run {
            reacts.sortedBy { it.priority }.forEach { addAll(it.getActions(player)) }
            this
        }
    }

    fun isEmpty(): Boolean {
        return reacts.isEmpty() || reacts.all { it.isEmpty() }
    }

    companion object {
        fun ofReaction(handle: ActionHandle, any: Any?): Reactions {
            val reacts = mutableListOf<Reaction>()
            any ?: return Reactions(handle, reacts)
            if (any is List<*>) {
                var order = 0
                any.filterNotNull().forEach { Reaction.of(handle, order++, it)?.let { react -> reacts.add(react) } }
            } else Reaction.of(handle, -1, any)?.let { reacts.add(it) }

            return Reactions(handle, reacts)
        }
    }

}