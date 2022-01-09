package menu.api.action.impl

import org.bukkit.entity.Player
import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.util.Bungees
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 11:41
 */
class ActionConnect(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Bungees.connect(player, parseContent(adaptPlayer(placeholderPlayer)))
    }

    companion object : ActionDesc {

        override val name = "bungee|server|connect".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionConnect(value.toString(), option)
        }

    }

}