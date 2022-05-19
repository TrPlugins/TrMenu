package trplugins.menu.api.action.impl.menu

import org.bukkit.Bukkit
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session

/**
 * TrMenu
 * trplugins.menu.api.action.impl.menu.SetAgent
 *
 * @author Score2
 * @since 2022/02/14 12:23
 */
class SetAgent(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(set|change)-?agent".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val agent = Bukkit.getPlayerExact(contents.stringContent().parseContent(placeholderPlayer)) ?: return

        player.session().agent = agent
    }
}