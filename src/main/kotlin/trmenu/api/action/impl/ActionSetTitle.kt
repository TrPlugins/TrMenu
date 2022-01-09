package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/2/1 23:27
 */
class ActionSetTitle(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.session()
        val receptacle = session.receptacle ?: return

        receptacle.title = parseContent(placeholderPlayer)
    }

    companion object : ActionDesc {

        override val name = "set-?title".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetTitle(value.toString(), option)
        }

    }

}