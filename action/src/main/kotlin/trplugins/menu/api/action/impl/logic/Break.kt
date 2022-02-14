package trplugins.menu.api.action.impl.logic

import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase

/**
 * TrMenu
 * trplugins.menu.api.action.impl.logic.Return
 *
 * @author Score2
 * @since 2022/02/10 22:09
 */
class Break(handle: ActionHandle) : ActionBase(handle) {
    override val regex = "return|break".toRegex()
}