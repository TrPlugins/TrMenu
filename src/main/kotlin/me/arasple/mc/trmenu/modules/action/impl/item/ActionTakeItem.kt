package me.arasple.mc.trmenu.modules.action.impl.item

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.item.ItemIdentifierHandler
import me.arasple.mc.trmenu.modules.item.impl.MatchItemAmount
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/4/4 15:14
 */
class ActionTakeItem : Action("(take|remove)(-)?item(s)?") {

    override fun onExecute(player: Player) {
        Tasks.run(true) {
            val ids = ItemIdentifierHandler.read(getContent()).identifiers

            println("Matches: $ids")

            player.inventory.contents.forEach {
                if (!Items.isNull(it)) {
                    val match = ids.firstOrNull { i -> i.match(player, it) }
                    if (match != null) {
                        val amt = NumberUtils.toInt(match.characteristic.firstOrNull { i -> i is MatchItemAmount }?.getContent(player), 1)
                        println("TAKING ITEM: $match, AMT: $amt")
                        Items.takeItem(player.inventory, { i: ItemStack -> i.isSimilar(it) }, amt)
                    }
                }
            }
        }
    }

}