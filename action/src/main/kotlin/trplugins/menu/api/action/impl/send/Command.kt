package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.chat.Command
 *
 * @author Score2
 * @since 2022/02/14 9:03
 */
class Command(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "command|cmd|player|execute".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        {
            placeholderPlayer.parseContentSplited(contents.stringContent(), ";").forEach {
                player.performCommand(it)
            }
        }.also {
            if (isPrimaryThread) {
                it.invoke()
            } else {
                submit(async = false) {
                    it.invoke()
                }
            }
        }
    }

}