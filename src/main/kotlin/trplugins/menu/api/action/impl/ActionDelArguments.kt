package trplugins.menu.api.action.impl

import org.bukkit.entity.Player
import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/2/8 22:25
 */
class ActionDelArguments : InternalAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        submit(delay = 3L, async = true) {
            player.session().arguments = arrayOf()
        }
    }

    companion object : ActionDesc {

        override val name = "(clear|cls|del|rem)-?arg(ument)?s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionDelArguments()
        }

    }

}