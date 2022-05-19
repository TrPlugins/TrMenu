package trplugins.menu.api.action.impl.hook

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.internal.hook.HookPlugin

/**
 * TrMenu
 * trplugins.menu.api.action.impl.hook.AddPoint
 *
 * @author Score2
 * @since 2022/02/14 14:18
 */
class AddPoint(handle: ActionHandle) : ActionBase(handle) {
    override val regex = "(give|add|deposit)-?points?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val amount = contents.stringContent().parseContent(placeholderPlayer).toIntOrNull() ?: -1
        if (amount > 0) {
            HookPlugin.getPlayerPoints().addPoints(player.cast(), amount)
        }
    }
}