package trplugins.menu.api.action.impl.metadaa

import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.internal.data.Metadata

/**
 * TrMenu
 * trplugins.menu.api.action.impl.metadaa.SetData
 *
 * @author Score2
 * @since 2022/02/14 14:28
 */
class SetData(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "set-?datas?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        contents.stringContent().parseContentSplited(placeholderPlayer, ";").forEach {
            val split = it.split(" ", limit = 2)
            if (split.size == 2) {
                val key = split[0]
                val value = split[1]

                Metadata.getData(player)[key] = value
            }
        }
    }

}