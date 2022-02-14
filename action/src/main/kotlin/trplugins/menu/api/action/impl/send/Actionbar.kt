package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.send.Actionbar
 *
 * @author Score2
 * @since 2022/02/08 21:33
 */
class Actionbar(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "action(bar)?s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        player.sendActionBar(contents.stringContent().parseContent(placeholderPlayer))
    }
}