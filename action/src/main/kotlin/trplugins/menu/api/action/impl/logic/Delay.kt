package trplugins.menu.api.action.impl.logic

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase

/**
 * TrMenu
 * trplugins.menu.api.action.impl.logic.Delay
 *
 * @author Score2
 * @since 2022/02/10 22:09
 */
class Delay(handle: ActionHandle) : ActionBase(handle) {
    override val regex = "delay|wait".toRegex()
    fun getDelay(player: ProxyPlayer, content: String) =
        player.parse(content).toLongOrNull() ?: 0L
}