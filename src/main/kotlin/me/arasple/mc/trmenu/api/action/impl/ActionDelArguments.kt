package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import taboolib.common.platform.submit

/**
 * @author Arasple
 * @date 2021/2/8 22:25
 */
class ActionDelArguments : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        submit(delay = 3L, async = true) {
            player.session().arguments = arrayOf()
        }
    }

    companion object {

        private val name = "(clear|cls|del|rem)-?arg(ument)?s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionDelArguments()
        }

        val registery = name to parser

    }

}