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
 * trplugins.menu.api.action.impl.metadaa.DelData
 *
 * @author Score2
 * @since 2022/02/14 14:27
 */
class DelData(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(remove|rem|del)-?datas?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val data = Metadata.getData(player).data

        contents.stringContent().parseContentSplited(placeholderPlayer, ";").forEach { it ->
            val regex = Regex(it)
            data.keys.filter { it.matches(regex) }.forEach { data.remove(it) }
        }
    }
}