package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session

/**
 * TrMenu
 * trplugins.menu.api.action.impl.inventory.Close
 *
 * @author Score2
 * @since 2022/02/14 11:13
 */
class Close(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "close|shut".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        player.session().close(closePacket = true, updateInventory = true)
    }

}