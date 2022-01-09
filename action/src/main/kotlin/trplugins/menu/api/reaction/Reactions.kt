package trplugins.menu.api.reaction

import trplugins.menu.api.action.base.AbstractAction
import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.Actions

/**
 * @author Arasple
 * @date 2021/1/29 17:51
 */
data class Reactions(private val reacts: List<Reaction>, val contentParser: trplugins.menu.util.function.ContentParser) {

    fun eval(player: ProxyPlayer): Boolean {
        if (isEmpty()) return true

        return Actions.runAction(player, getActions(player))
    }

    fun getActions(player: ProxyPlayer): List<AbstractAction> {
        return mutableListOf<AbstractAction>().run {
            reacts.sortedBy { it.priority }.forEach { addAll(it.getActions(player)) }
            this
        }
    }

    fun isEmpty(): Boolean {
        return reacts.isEmpty() || reacts.all { it.isEmpty() }
    }

    companion object

}