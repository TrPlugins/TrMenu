package trplugins.menu.api.action.impl.metadaa

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.internal.data.Metadata
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 19:58
 * key support regex
 * del-data: KEY
 */
class ActionGlobalDataDel(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val data = Metadata.globalData

        parseContentSplited(adaptPlayer(placeholderPlayer), ";").forEach { it ->
            val regex = Regex(it)
            data.getKeys(true).filter { it.matches(regex) }.forEach { data[it] = null }
        }
    }

    companion object : ActionDesc {

        override val name = "(remove|rem|del)-?(global|g)-?datas?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionGlobalDataDel(value.toString(), option)
        }

    }

}