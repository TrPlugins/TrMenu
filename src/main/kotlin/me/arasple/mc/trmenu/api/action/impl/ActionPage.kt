package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import kotlin.math.min

/**
 * @author Arasple
 * @date 2021/2/1 23:22
 */
class ActionPage(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.getSession()
        val menu = session.menu ?: return
        val page = min(parseContent(placeholderPlayer).toIntOrNull() ?: 0, menu.layout.getSize() - 1)

        menu.page(player, page)
    }

    companion object {

        private val name = "(set|switch)?-?(layout|shape|page)s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionPage(value.toString(), option)
        }

        val registery = name to parser

    }

}