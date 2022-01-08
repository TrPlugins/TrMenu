package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer

/**
 * @author Arasple
 * @date 2021/1/31 11:36
 */
class ActionActionbar(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        adaptPlayer(player).sendActionBar(parseContent(placeholderPlayer))
    }

    companion object {

        private val name = "action(bar)?s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionActionbar(value.toString(), option)
        }

        val registery = name to parser

    }

}