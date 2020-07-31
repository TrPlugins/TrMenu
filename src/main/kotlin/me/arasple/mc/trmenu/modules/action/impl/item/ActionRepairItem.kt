package me.arasple.mc.trmenu.modules.action.impl.item

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*


/**
 * @author Rubenicos
 * @date 2020/7/30 17:30
 */
class ActionRepairItem : Action("repair(-)?item(s)?") {
    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach {
        if (it.toLowerCase() == "all") {
            val items: MutableList<ItemStack> = ArrayList()
            player.inventory.contents.forEach { slot ->
                if (slot.type != Material.AIR && !slot.type.isBlock && !slot.type.isEdible && slot.type.maxDurability <= 0) items.add(slot)
            }
            if (items.isNotEmpty()) {
                items.forEach {item ->
                    item.durability = 0.toShort()
                    player.updateInventory() // <--- maybe?
                }
            }
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
            if (item != null && item.type != Material.AIR && !item.type.isBlock && !item.type.isEdible && item.type.maxDurability <= 0) {
                    item.durability = 0.toShort()
                    player.updateInventory() // <--- maybe?
            }
        }
    }
}
