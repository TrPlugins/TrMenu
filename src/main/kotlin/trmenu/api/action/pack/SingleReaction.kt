package trmenu.api.action.pack

import trmenu.api.action.base.AbstractAction
import org.bukkit.entity.Player

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
class SingleReaction(priority: Int, private val actions: List<AbstractAction>) : Reaction(priority) {

    override fun isEmpty(): Boolean {
        return actions.isEmpty()
    }

    override fun getActions(player: Player): List<AbstractAction> {
        return actions
    }
}