package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/8 22:25
 */
class ActionDelArguments : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        player.getSession().arguments = arrayOf()
    }

    companion object {

        private val name = "(clear|cls|del|rem)-?arg(ument)?s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionDelArguments()
        }

        val registery = name to parser

    }

}