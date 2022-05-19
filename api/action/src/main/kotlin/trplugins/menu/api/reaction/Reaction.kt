package trplugins.menu.api.reaction

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionEntry
import trplugins.menu.util.conf.Property

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
abstract class Reaction(val handle: ActionHandle, val priority: Int) {

    abstract fun isEmpty(): Boolean

    abstract fun getActions(player: ProxyPlayer): List<ActionEntry>

    companion object {
        fun of(handle: ActionHandle, priority: Int, any: Any): Reaction? {
            if (any is String || ((any is Map<*, *>) && any.entries.firstOrNull()?.key.toString().equals("catcher", true))) {
                return SingleReaction(handle, priority, ActionEntry.of(handle, any))
            }

            val reaction = Property.asSection(any) ?: return null
            val keyPriority = Property.PRIORITY.getKey(reaction)
            val keyRequirement = Property.CONDITION.getKey(reaction)
            val keyActions = Property.ACTIONS.ofList(reaction)
            val keyDenyActions = Property.DENY_ACTIONS.ofList(reaction)

            return ConditionalReaction(
                handle,
                reaction.getInt(keyPriority, priority),
                reaction.getString(keyRequirement, "")!!,
                Reactions.ofReaction(handle, keyActions),
                Reactions.ofReaction(handle, keyDenyActions)
            )
        }
    }
}