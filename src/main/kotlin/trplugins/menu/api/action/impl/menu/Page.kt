package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session
import kotlin.math.min

/**
 * TrMenu
 * trplugins.menu.api.action.impl.inventory.Page
 *
 * @author Score2
 * @since 2022/02/14 11:14
 */
class Page(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(set|switch)?-?(layout|shape|page)s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val session = player.session()
        val menu = session.menu ?: return
        val page = min(contents.stringContent().parseContent(placeholderPlayer).toIntOrNull() ?: 0, menu.layout.getSize() - 1)

        menu.page(player.cast(), page)
    }

}