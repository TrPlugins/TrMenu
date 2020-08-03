package me.arasple.mc.trmenu.modules.action.impl.item

import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.item.ItemIdentifier
import me.arasple.mc.trmenu.modules.item.ItemIdentifierHandler
import me.arasple.mc.trmenu.utils.Tasks
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
        Tasks.task(true) {
            player.inventory.contents.forEach {
                if (!Items.isNull(it)) {
                    val match = ids.firstOrNull { i -> i.match(player, it) }
                    if (match != null) {
                        val amt = match.amount(player)
                        Items.takeItem(
                            player.inventory,
                            { i -> i.isSimilar(it) },
                            amt
                        )
                    }
                }
            }
        }
    }

}