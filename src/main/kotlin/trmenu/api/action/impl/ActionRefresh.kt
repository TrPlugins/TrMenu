package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/2 11:05
 */
class ActionRefresh(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.session()

        if (baseContent.isBlank() || baseContent.equals("refresh", true)) {
            session.activeIcons.forEach { it.onRefresh(session) }
        } else {
            baseContent.split(";").mapNotNull { session.getIcon(it) }.forEach { it.onRefresh(session) }
        }
    }

    companion object {

        private val name = "(icon)?-?refresh".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionRefresh(value.toString(), option)
        }

        val registery = name to parser

    }

}