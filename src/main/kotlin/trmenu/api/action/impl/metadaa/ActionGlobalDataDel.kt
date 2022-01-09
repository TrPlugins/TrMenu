package trmenu.api.action.impl.metadaa

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 19:58
 * key support regex
 * del-data: KEY
 */
class ActionGlobalDataDel(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val data = Metadata.globalData

        parseContentSplited(placeholderPlayer, ";").forEach { it ->
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