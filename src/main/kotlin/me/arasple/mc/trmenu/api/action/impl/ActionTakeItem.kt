package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.util.bukkit.ItemMatcher
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 16:25
 */
class ActionTakeItem(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        ItemMatcher.eval(parseContent(placeholderPlayer), !dynamic).takeItem(player)
    }

    companion object {

        private val name = "(take|remove)(-)?items?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionTakeItem(value.toString(), option)
        }

        val registery = name to parser

    }

}