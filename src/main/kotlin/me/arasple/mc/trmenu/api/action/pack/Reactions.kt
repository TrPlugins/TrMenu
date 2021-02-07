package me.arasple.mc.trmenu.api.action.pack

import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.module.conf.prop.Property
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.script.Condition
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 17:51
 */
inline class Reactions(private val reacts: List<React>) {

    companion object {

        fun ofReaction(any: Any?): Reactions {
            val reacts = mutableListOf<React>()
            any ?: return Reactions(reacts)
            if (any is List<*>) {
                val first = any.firstOrNull()
                if (first is String || ((first is Map<*, *>) && first.entries.firstOrNull()?.key.toString().equals("catcher", true))) {
                    return Reactions(
                        listOf(
                            React(
                                -1, "", mutableListOf<AbstractAction>().run {
                                    any.filterNotNull().forEach { addAll(Actions.readAction(it)) }
                                    this
                                }, listOf()
                            )
                        )
                    )
                }
                var order = 0
                any.filterNotNull().forEach { ofReact(order++, it)?.let { react -> reacts.add(react) } }
            } else ofReact(-1, any)?.let { reacts.add(it) }

            return Reactions(reacts)
        }

        private fun ofReact(priority: Int, any: Any): React? {
            if (any is String) return React(priority, "", Actions.readAction(any), listOf())

            val reaction = Property.asSection(any) ?: return null
            val keyPriority = Property.PRIORITY.getKey(reaction)
            val keyRequirement = Property.CONDITION.getKey(reaction)
            val keyActions = Property.ACTIONS.getKey(reaction)
            val keyDenyActions = Property.DENY_ACTIONS.getKey(reaction)

            return React(
                reaction.getInt(keyPriority, priority),
                reaction.getString(keyRequirement, "")!!,
                Actions.readAction(Property.asList(reaction[keyActions])),
                Actions.readAction(Property.asList(reaction[keyDenyActions]))
            )
        }

    }

    fun eval(session: MenuSession): Boolean {
        return eval(session.viewer)
    }

    fun eval(player: Player): Boolean {
        if (isEmpty()) return true
        val reacts = reacts.sortedBy { it.priority }

        reacts.forEach {
            if (!it.react(player)) return false
        }

        return true
    }

    fun isEmpty(): Boolean {
        return reacts.isEmpty() || reacts.all { it.isEmpty }
    }


    /**
     * Single
     */
    data class React(
        val priority: Int,
        private val condition: String,
        private val accept: List<AbstractAction>,
        private val deny: List<AbstractAction>
    ) {

        private val hasCondition: Boolean = condition.isNotBlank()
        val isEmpty: Boolean = accept.isEmpty() && deny.isEmpty()

        fun react(player: Player): Boolean {
            return if (evalCondition(player)) Actions.runAction(player, accept)
            else Actions.runAction(player, deny)
        }

        private fun evalCondition(player: Player): Boolean {
            return if (hasCondition) Condition.eval(player, condition).asBoolean()
            else true
        }

    }

}