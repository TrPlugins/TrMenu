package trplugins.menu.api.action

import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.api.reaction.ConditionalReaction
import trplugins.menu.api.reaction.Reaction
import trplugins.menu.api.reaction.Reactions
import trplugins.menu.api.reaction.SingleReaction
import trplugins.menu.module.conf.prop.Property
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.script.contentParser

/**
 * TrMenu
 * trmenu.api.action.InternalAction
 *
 * @author Score2
 * @since 2022/01/09 19:44
 */
abstract class InternalAction(
    baseContent: String = "",
    option: ActionOption = ActionOption()
) : AbstractAction(baseContent, option) {
    
    abstract fun onExecute(player: Player, placeholderPlayer: Player = player)

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        onExecute(player.cast<Player>(), placeholderPlayer.cast())
    }
    
}

fun Reactions.eval(session: MenuSession): Boolean {
    return eval(adaptPlayer(session.viewer))
}

fun Reactions.eval(player: Player): Boolean {
    if (isEmpty()) return true

    return Actions.runAction(player, getActions(adaptPlayer(player)))
}

fun Reaction.Companion.of(priority: Int, any: Any): Reaction? {
    if (any is String || ((any is Map<*, *>) && any.entries.firstOrNull()?.key.toString().equals("catcher", true))) {
        return SingleReaction(priority, Actions.readAction(any))
    }

    val reaction = Property.asSection(any) ?: return null
    val keyPriority = Property.PRIORITY.getKey(reaction)
    val keyRequirement = Property.CONDITION.getKey(reaction)
    val keyActions = Property.ACTIONS.ofList(reaction)
    val keyDenyActions = Property.DENY_ACTIONS.ofList(reaction)

    return ConditionalReaction(
        reaction.getInt(keyPriority, priority),
        reaction.getString(keyRequirement, "")!!,
        Reactions.ofReaction(keyActions),
        Reactions.ofReaction(keyDenyActions)
    )
}


fun Reactions.Companion.ofReaction(any: Any?): Reactions {
    val reacts = mutableListOf<Reaction>()
    any ?: return Reactions(reacts, contentParser)
    if (any is List<*>) {
        var order = 0
        any.filterNotNull().forEach { Reaction.of(order++, it)?.let { react -> reacts.add(react) } }
    } else Reaction.of(-1, any)?.let { reacts.add(it) }

    return Reactions(reacts, contentParser)
}

fun Actions.Companion.runAction(player: Player, actions: List<String>) =
    runAction(adaptPlayer(player), actions)

fun Actions.Companion.runAction(player: Player, actions: List<AbstractAction>) =
    runAction(adaptPlayer(player), actions)