package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.chat.Tell
 *
 * @author Score2
 * @since 2022/02/14 9:05
 */
class Tell(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "tell|message|msg|talk".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        contents.stringContent().parseContentSplited(placeholderPlayer).forEach {
            player.sendMessage(it)
        }
    }

}