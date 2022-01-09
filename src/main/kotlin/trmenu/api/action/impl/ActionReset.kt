package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.display.Menu
import org.bukkit.entity.Player
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/2/8 21:34
 */
class ActionReset(option: ActionOption) : AbstractAction(option = option) {

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