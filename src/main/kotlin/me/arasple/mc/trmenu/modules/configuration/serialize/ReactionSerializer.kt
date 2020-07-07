package me.arasple.mc.trmenu.modules.configuration.serialize

import me.arasple.mc.trmenu.display.function.Reaction
import me.arasple.mc.trmenu.display.function.Reactions
import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.modules.configuration.property.Property
import me.arasple.mc.trmenu.utils.Utils

/**
 * @author Arasple
 * @date 2020/7/7 10:15
 */
object ReactionSerializer {

    fun serializeReactions(any: Any?): Reactions = Reactions(serializeReactionsList(any))

    fun serializeReactionsList(any: Any?): List<Reaction> = mutableListOf<Reaction>().let { reactions ->
        if (any is List<*>) {
            any.forEach {
                if (it != null) serializeReaction(it)?.let { reaction -> reactions.add(reaction) }
            }
        } else if (any != null) serializeReaction(any)?.let { reactions.add(it) }
        return@let reactions
    }

    fun serializeReaction(any: Any): Reaction? {
        val reaction = Utils.asSection(any) ?: return null

        val keyPriority = Utils.getSectionKey(reaction, Property.PRIORITY)
        val keyRequirement = Utils.getSectionKey(reaction, Property.REQUIREMENT)
        val keyActions = Utils.getSectionKey(reaction, Property.ACTIONS)
        val keyDenyActions = Utils.getSectionKey(reaction, Property.DENY_ACTIONS)

        return Reaction(
            reaction.getInt(keyPriority, -1),
            reaction.getString(keyRequirement) ?: "",
            Actions.readActions(reaction.getStringList(keyActions)),
            Actions.readActions(reaction.getStringList(keyDenyActions))
        )
    }


}