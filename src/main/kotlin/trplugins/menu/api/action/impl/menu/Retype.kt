package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.internal.data.Metadata

/**
 * TrMenu
 * trplugins.menu.api.action.impl.receptacle.Retype
 *
 * @author Score2
 * @since 2022/02/14 12:18
 */
class Retype(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "re-?(peat|catcher|input|enter|type)s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        Metadata.setBukkitMeta(player.cast(), "RE_ENTER")
    }
}