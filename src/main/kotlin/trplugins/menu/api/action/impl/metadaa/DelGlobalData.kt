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
 * trplugins.menu.api.action.impl.metadaa.DelGlobalData
 *
 * @author Score2
 * @since 2022/02/14 14:28
 */
class DelGlobalData(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(remove|rem|del)-?(global|g)-?datas?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val data = Metadata.globalData

        contents.stringContent().parseContentSplited(placeholderPlayer, ";").forEach { it ->
            val regex = Regex(it)
            data.getKeys(true).filter { it.matches(regex) }.forEach { data[it] = null }
        }
    }
}