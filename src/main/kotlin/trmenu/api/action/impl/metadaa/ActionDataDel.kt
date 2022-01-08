package trmenu.api.action.impl.metadaa

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 19:58
 * key support regex
 * del-data: KEY
 */
class ActionDataDel(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val data = Metadata.getData(player).data

        parseContentSplited(placeholderPlayer, ";").forEach { it ->
            val regex = Regex(it)
            data.keys.filter { it.matches(regex) }.forEach { data.remove(it) }
        }
    }

    companion object {

        private val name = "(remove|rem|del)-?datas?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionDataDel(value.toString(), option)
        }

        val registery = name to parser

    }

}