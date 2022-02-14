package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.console
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.chat.CommandConsole
 *
 * @author Score2
 * @since 2022/02/14 9:04
 */
class Console(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "console".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        {
            contents.stringContent().parseContentSplited(placeholderPlayer, ";").forEach {
                console().performCommand(it)
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