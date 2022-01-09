package trplugins.menu.api.action.impl.func

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.util.bukkit.ItemMatcher
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/1/31 16:25
 */
class ActionGiveItem(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        ItemMatcher.eval(parseContent(adaptPlayer(placeholderPlayer)), !parsable).buildItem().forEach {
            player.inventory.addItem(it).values.forEach { e -> player.world.dropItem(player.location, e) }
        }
        player.session().playerItemSlots()
    }

    companion object : ActionDesc {

        override val name = "(give|add)(-)?items?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionGiveItem(value.toString(), option)
        }

    }

}