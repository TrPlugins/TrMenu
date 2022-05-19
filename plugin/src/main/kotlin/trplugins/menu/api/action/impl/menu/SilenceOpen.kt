package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.internal.data.Metadata

/**
 * TrMenu
 * trplugins.menu.api.action.impl.inventory.SilenceOpen
 *
 * @author Score2
 * @since 2022/02/14 11:15
 */
class SilenceOpen(handle: ActionHandle) : ActionBase(handle) {

    val open = Open(handle)

    override val regex = "(force|silent)-?(open|menu)".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        Metadata.setBukkitMeta(player.cast(), "FORCE_OPEN")
        open.onExecute(contents, player)
    }

}