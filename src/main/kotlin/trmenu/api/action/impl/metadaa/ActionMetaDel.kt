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
 * del-meta: KEY
 */
class ActionMetaDel(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val data = Metadata.getMeta(player).data

        parseContentSplited(placeholderPlayer, ";").forEach { it ->
            val regex = Regex(it)
            data.keys.filter { it.matches(regex) }.forEach { data.remove(it) }
        }
    }

    companion object : ActionDesc {

        override val name = "(remove|rem|del)-?(temp|var(iable)?|meta)s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionMetaDel(value.toString(), option)
        }

    }

}