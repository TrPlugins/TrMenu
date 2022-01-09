package trplugins.menu.api.reaction

import trplugins.menu.api.action.base.AbstractAction
import taboolib.common.platform.ProxyPlayer

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
abstract class Reaction(val priority: Int) {

    abstract fun isEmpty(): Boolean

    abstract fun getActions(player: ProxyPlayer): List<AbstractAction>

    companion object
}