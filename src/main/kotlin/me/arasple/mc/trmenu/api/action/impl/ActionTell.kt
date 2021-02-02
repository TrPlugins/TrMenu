package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 18:01
 */
class ActionTell(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(placeholderPlayer).forEach {
            player.sendMessage(it)
        }
    }

    companion object {

        private val name = "tell|message|msg|talk".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionTell(value.toString(), option)
        }

        val registery = name to parser

    }

}