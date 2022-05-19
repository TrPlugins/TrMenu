package trplugins.menu.api.action.impl.script

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.TrMenuAPI
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.script.Kether
 *
 * @author Score2
 * @since 2022/02/14 12:55
 */
class Kether(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "ke(ther)?s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        TrMenuAPI.eval(player.cast(), contents.stringContent())
    }
}