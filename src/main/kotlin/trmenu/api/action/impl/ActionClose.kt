package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 22:25
 */
class ActionClose : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        player.session().close(closePacket = true, updateInventory = true)
    }

    companion object {

        private val name = "close|shut".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionClose()
        }

        val registery = name to parser

    }

}