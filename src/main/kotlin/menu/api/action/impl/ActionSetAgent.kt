package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc
import trmenu.module.display.session

/**
 * @author Rubenicos
 * @date 2022/1/1 11:40
 */
class ActionSetAgent(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val agent = Bukkit.getPlayerExact(parseContent(adaptPlayer(placeholderPlayer))) ?: return

        player.session().agent = agent
    }

    companion object : ActionDesc {

        override val name = "(set|change)-?agent".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetAgent(value.toString(), option)
        }

    }
}