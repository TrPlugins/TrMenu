package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.Menu
import trplugins.menu.module.display.session

/**
 * TrMenu
 * trplugins.menu.api.action.impl.inventory.Reset
 *
 * @author Score2
 * @since 2022/02/14 11:16
 */
class Reset(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "resets?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val session = player.session()

        Menu.menus.forEach {
            it.icons.forEach { icon ->
                icon.onReset(session)
            }
        }
    }

}