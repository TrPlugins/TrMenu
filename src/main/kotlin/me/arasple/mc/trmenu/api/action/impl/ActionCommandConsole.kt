package me.arasple.mc.trmenu.api.action.impl

import io.izzel.taboolib.util.Features
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 11:38
 */
class ActionCommandConsole(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(placeholderPlayer, ";").forEach {
            Features.dispatchCommand(Bukkit.getConsoleSender(), it)
        }
    }

    companion object {

        private val name = "console".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionCommandConsole(value.toString(), option)
        }

        val registery = name to parser

    }

}