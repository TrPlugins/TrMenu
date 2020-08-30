package me.arasple.mc.trmenu.modules.conf.serialize

import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.modules.conf.property.Property
import me.arasple.mc.trmenu.modules.display.function.Reaction
import me.arasple.mc.trmenu.modules.display.function.Reactions
import me.arasple.mc.trmenu.util.Utils

/**
 * @author Arasple
 * @date 2020/7/7 10:15
 */
object ReactionSerializer {

    fun serializeReactions(any: Any?) = Reactions(serializeReactionsList(any))

    fun serializeReactionsList(any: Any?) = mutableListOf<Reaction>().let { reactions ->
        if (any is List<*>) {
            val first = any.firstOrNull()
            if (first is String || (first is Map<*, *>) && first.entries.firstOrNull()?.key.toString()
                    .equals("catcher", true)
            ) {
                reactions.add(
                    Reaction(-1, "", mutableListOf<Action>().let {
                        any.forEach { action -> it.addAll(Actions.readAction(action)) }
                        it
                    }, listOf())
                )
                return@let reactions
            }
            var order = any.size
            any.filterNotNull().forEach {
                serializeReaction(order--, it)?.let { reaction -> reactions.add(reaction) }
            }
        } else if (any != null) serializeReaction(-1, any)?.let { reactions.add(it) }
        return@let reactions
    }

    fun serializeReaction(priority: Int, any: Any): Reaction? {
        if (any is String) return Reaction(priority, "", Actions.readAction(any), listOf())

        val reaction = Utils.asSection(any) ?: return null
        val keyPriority = Utils.getSectionKey(reaction, Property.PRIORITY)
        val keyRequirement = Utils.getSectionKey(reaction, Property.REQUIREMENT)
        val keyActions = Utils.getSectionKey(reaction, Property.ACTIONS)
        val keyDenyActions = Utils.getSectionKey(reaction, Property.DENY_ACTIONS)

        return Reaction(
            reaction.getInt(keyPriority, priority),
            reaction.getString(keyRequirement) ?: "",
            Actions.readActions(Utils.asAnyList(reaction.get(keyActions))),
            Actions.readActions(Utils.asAnyList(reaction.get(keyDenyActions)))
        )
    }


}