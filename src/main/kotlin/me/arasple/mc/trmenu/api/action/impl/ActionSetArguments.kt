package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/5 8:55
 */
class ActionSetArguments(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        player.getSession().arguments =
            parseContentSplited(placeholderPlayer)
                .map { it.replace("\\s", " ") }
                .toTypedArray()
    }

    companion object {

        private val name = "set-?arg(ument)?s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetArguments(value.toString(), option)
        }

        val registery = name to parser

    }

}