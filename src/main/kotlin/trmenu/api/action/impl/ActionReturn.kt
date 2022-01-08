package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 21:22
 */
class ActionReturn : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        TODO("Not yet implemented")
    }

    companion object {

        private val name = "return|break".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ -> ActionReturn() }

        val registery = name to parser

    }

}