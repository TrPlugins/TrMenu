package trmenu.api.action.impl

import trmenu.api.TrMenuAPI
import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/2/1 12:31
 */
class ActionKether(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        TrMenuAPI.eval(player, baseContent)
    }

    companion object : ActionDesc {

        override val name = "ke(ther)?s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { it, option ->
            ActionKether(it.toString(), option)
        }

    }

}
