package trplugins.menu.api.action.impl

import org.bukkit.entity.Player
import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.util.Bungees
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc

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