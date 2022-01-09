package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.display.Menu
import org.bukkit.entity.Player
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/2/8 21:34
 */
class ActionReset(option: ActionOption) : InternalAction(option = option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.session()

        Menu.menus.forEach {
            it.icons.forEach { icon ->
                icon.onReset(session)
            }
        }
    }

    companion object : ActionDesc {

        override val name = "resets?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { _, option ->
            ActionReset(option)
        }

    }

}