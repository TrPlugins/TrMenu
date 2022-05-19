package trplugins.menu.api.action.impl.send

import org.bukkit.Bukkit
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.send.CommandOp
 *
 * @author Score2
 * @since 2022/02/14 12:29
 */
class CommandOp(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "op(erator)?s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        {
            contents.stringContent().parseContentSplited(placeholderPlayer, ";").forEach {
                player.isOp.let { isOp ->
                    player.isOp = true
                    try {
                        player.performCommand(it)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                    player.isOp = isOp
                }
            }
        }.also {
            if (Bukkit.isPrimaryThread()) {
                it.invoke()
            } else {
                submit(async = false) {
                    it.invoke()
                }
            }
        }
    }

}