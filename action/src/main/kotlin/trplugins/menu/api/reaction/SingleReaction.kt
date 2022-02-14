package trplugins.menu.api.reaction

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionEntry

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
class SingleReaction(
    handle: ActionHandle,
    priority: Int,
    private val actions: List<ActionEntry>
) : Reaction(handle, priority) {

    override fun isEmpty(): Boolean {
        return actions.isEmpty()
    }

    override fun getActions(player: ProxyPlayer): List<ActionEntry> {
        return actions
    }
}