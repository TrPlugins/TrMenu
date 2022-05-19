package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session

/**
 * TrMenu
 * trplugins.menu.api.action.impl.receptacle.ClearArgument
 *
 * @author Score2
 * @since 2022/02/14 12:05
 */
class DelArguments(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(clear|cls|del|rem)-?arg(ument)?s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        submit(delay = 3L, async = true) {
            player.session().arguments = arrayOf()
        }
    }
}