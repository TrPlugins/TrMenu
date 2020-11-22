package me.arasple.mc.trmenu.api.action.impl.item

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.action.base.Action
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


/**
 * @author Rubenicos
 * @date 2020/7/30 17:30
 */
class ActionRepairItem : Action("repair(-)?item(s)?") {

    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach {
        if (it.toLowerCase() == "all") {
            player.inventory.contents.forEach { item ->
                if (item != null) {
                    repair(item)
                }
            }
            player.updateInventory()
        } else if (it.toLowerCase() == "armor") {
            player.inventory.armorContents.forEach { item ->
                if (item != null) {
                    repair(item)
                }
            }
            player.updateInventory()
        } else {
            val item: ItemStack? = when (it.toLowerCase()) {
                "hand" -> player.inventory.itemInMainHand
                "offhand" -> player.inventory.itemInOffHand
                "helmet" -> player.inventory.armorContents[3]
                "chestplate" -> player.inventory.armorContents[2]
                "leggings" -> player.inventory.armorContents[1]
                "boots" -> player.inventory.armorContents[0]
                else -> player.inventory.getItem(NumberUtils.toInt(it, 0))
            }
            if (item != null) {
                repair(item)
                player.updateInventory()
            }
        }
    }

    private fun repair(item: ItemStack) {
        @Suppress("DEPRECATION")
        if (item.durability.toString() != "0" && !item.type.toString().contains("AIR") && !item.type.isBlock && !item.type.isEdible) {
            item.durability = 0.toShort()
        }
    }

}
