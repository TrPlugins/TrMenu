package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.chat.Chat
 *
 * @author Score2
 * @since 2022/02/14 9:03
 */
class Chat(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "chat|send|say".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        contents.stringContent().parseContentSplited(placeholderPlayer, contents.stringContent()).forEach {
            player.chat(it)
        }
    }

}