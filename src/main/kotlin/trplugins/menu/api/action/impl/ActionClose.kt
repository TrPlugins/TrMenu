package trplugins.menu.api.action.impl

import org.bukkit.entity.Player
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/1/29 22:25
 */
class ActionClose : InternalAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        player.session().close(closePacket = true, updateInventory = true)
    }

    companion object : ActionDesc {

        override val name = "close|shut".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionClose()
        }

    }

}