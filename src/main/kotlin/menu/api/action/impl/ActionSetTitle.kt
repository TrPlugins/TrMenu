package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc
import trmenu.module.display.session

/**
 * @author Arasple
 * @date 2021/2/1 23:27
 */
class ActionSetTitle(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.session()
        val receptacle = session.receptacle ?: return

        receptacle.title = parseContent(adaptPlayer(placeholderPlayer))
    }

    companion object : ActionDesc {

        override val name = "set-?title".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetTitle(value.toString(), option)
        }

    }

}