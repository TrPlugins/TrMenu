package me.arasple.mc.trmenu.modules.action.impl.item

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.item.ItemIdentifierHandler
import me.arasple.mc.trmenu.modules.item.impl.MatchItemAmount
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/4 15:14
 */
class ActionTakeItem : Action("(take|remove)(-)?item(s)?") {

    override fun onExecute(player: Player) {
        Tasks.task(true) {
            val ids = ItemIdentifierHandler.read(getContent(player)).identifiers
            player.inventory.contents.forEach {
                if (!Items.isNull(it)) {
                    val match = ids.firstOrNull { i -> i.match(player, it) }
                    if (match != null) {
                        val amt = NumberUtils.toInt(match.characteristic.firstOrNull { i -> i is MatchItemAmount }?.getContent(player), 1)
                        Items.takeItem(player.inventory, { i -> i.isSimilar(it) }, amt)
                    }
                }
            }
        }
    }

}