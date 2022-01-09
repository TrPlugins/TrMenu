package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc
import trmenu.module.display.session
import kotlin.math.min

/**
 * @author Arasple
 * @date 2021/2/1 23:22
 */
class ActionPage(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.session()
        val menu = session.menu ?: return
        val page = min(parseContent(adaptPlayer(placeholderPlayer)).toIntOrNull() ?: 0, menu.layout.getSize() - 1)

        menu.page(player, page)
    }

    companion object : ActionDesc {

        override val name = "(set|switch)?-?(layout|shape|page)s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionPage(value.toString(), option)
        }

    }

}