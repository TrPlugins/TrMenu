package trmenu.api.action.impl.func

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.util.bukkit.ItemMatcher
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 16:25
 */
class ActionTakeItem(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        ItemMatcher.eval(parseContent(placeholderPlayer), !parsable).takeItem(player)
        player.session().playerItemSlots()
    }

    companion object {

        private val name = "(take|remove)(-)?items?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionTakeItem(value.toString(), option)
        }

        val registery = name to parser

    }

}