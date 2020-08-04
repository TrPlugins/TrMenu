package me.arasple.mc.trmenu.modules.action.impl.item

import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.item.ItemIdentifier
import me.arasple.mc.trmenu.modules.item.ItemIdentifierHandler
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/4 15:14
 */
class ActionTakeItem : Action("(take|remove)(-)?item(s)?") {

    lateinit var ids: Set<ItemIdentifier.Identifier>

    override fun setContent(content: String) {
        super.setContent(content)
        ids = ItemIdentifierHandler.read(content).identifiers
    }

    override fun onExecute(player: Player) {
        ids.firstOrNull()?.take(player)
    }

}