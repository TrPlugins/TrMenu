package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session

/**
 * TrMenu
 * trplugins.menu.api.action.impl.inventory.Refresh
 *
 * @author Score2
 * @since 2022/02/14 11:15
 */
class Refresh(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(icon)?-?refresh".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val session = player.session()
        val baseContent = contents.stringContent()

        if (baseContent.isBlank() || baseContent.equals("refresh", true)) {
            session.activeIcons.forEach { it.onRefresh(session) }
        } else {
            baseContent.split(";").mapNotNull { session.getIcon(it) }.forEach { it.onRefresh(session) }
        }
    }
}