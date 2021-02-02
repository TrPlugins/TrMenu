package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/1 23:27
 */
class ActionSetTitle(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.getSession()
        val receptacle = session.receptacle ?: return

        receptacle.title = parseContent(placeholderPlayer)
    }

    companion object {

        private val name = "set-?title".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetTitle(value.toString(), option)
        }

        val registery = name to parser

    }

}