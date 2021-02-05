package me.arasple.mc.trmenu.api.action.impl.func

import io.izzel.taboolib.cronus.CronusUtils
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.util.bukkit.ItemMatcher
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 16:25
 */
class ActionGiveItem(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        ItemMatcher.eval(parseContent(placeholderPlayer), !dynamic).buildItem().forEach {
            CronusUtils.addItem(player, it)
        }
    }

    companion object {

        private val name = "(give|add)(-)?items?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionGiveItem(value.toString(), option)
        }

        val registery = name to parser

    }

}