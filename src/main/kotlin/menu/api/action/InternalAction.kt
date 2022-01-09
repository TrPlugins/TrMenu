package menu.api.action

import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.api.reaction.ConditionalReaction
import trmenu.api.reaction.Reaction
import trmenu.api.reaction.Reactions
import trmenu.api.reaction.SingleReaction
import trmenu.module.conf.prop.Property
import trmenu.module.display.MenuSession
import trmenu.module.internal.script.contentParser

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
        onExecute(player.cast<Player>(), placeholderPlayer.cast<Player>())
    }
    
}

fun Reactions.eval(session: MenuSession): Boolean {
    return eval(adaptPlayer(session.viewer))
}

fun Reactions.eval(player: Player): Boolean {
    if (isEmpty()) return true

    return menu.api.action.Actions.runAction(player, getActions(adaptPlayer(player)))
}

fun Reaction.Companion.of(priority: Int, any: Any): Reaction? {
    if (any is String || ((any is Map<*, *>) && any.entries.firstOrNull()?.key.toString().equals("catcher", true))) {
        return SingleReaction(priority, menu.api.action.Actions.readAction(any))
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

fun menu.api.action.Actions.Companion.runAction(player: Player, actions: List<String>) =
    menu.api.action.Actions.runAction(adaptPlayer(player), actions)

fun menu.api.action.Actions.Companion.runAction(player: Player, actions: List<AbstractAction>) =
    menu.api.action.Actions.runAction(adaptPlayer(player), actions)