package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc
import trmenu.module.display.session

/**
 * @author Arasple
 * @date 2021/2/2 11:05
 */
class ActionRefresh(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.session()

        if (baseContent.isBlank() || baseContent.equals("refresh", true)) {
            session.activeIcons.forEach { it.onRefresh(session) }
        } else {
            baseContent.split(";").mapNotNull { session.getIcon(it) }.forEach { it.onRefresh(session) }
        }
    }

    companion object : ActionDesc {

        override val name = "(icon)?-?refresh".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionRefresh(value.toString(), option)
        }

    }

}