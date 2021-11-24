package me.arasple.mc.trmenu.api.action.pack

import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import org.bukkit.entity.Player

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
class SingleReaction(priority: Int, private val actions: List<AbstractAction>) : Reaction(priority) {

    override fun isEmpty(): Boolean {
        return actions.isEmpty()
    }

    override fun react(player: Player): Boolean {
        return Actions.runAction(player, actions)
    }
}