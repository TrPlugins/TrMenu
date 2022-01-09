package menu.api.action.impl

import org.bukkit.entity.Player
import trmenu.api.action.InternalAction
import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionDesc
import trmenu.api.action.base.ActionOption
import trmenu.module.display.session

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