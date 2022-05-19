package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.util.Bungees

/**
 * TrMenu
 * trplugins.menu.api.action.impl.send.Connect
 *
 * @author Score2
 * @since 2022/02/14 12:29
 */
class Connect(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "bungee|server|connect".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        Bungees.connect(player.cast(), contents.stringContent().parseContent(placeholderPlayer))
    }
}