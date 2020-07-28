package me.arasple.mc.trmenu.modules.action.impl.item

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Rubenicos
 * @date 2020/7/27 10:50
 */
class ActionEnchantItem : Action("enchant(-)?item(s)?") {

    override fun onExecute(player: Player) = getContentSplited(player, ";").forEach {
        val part = it.split(",").toTypedArray()
        val enchant = Enchantment.getByName(part[1].toUpperCase())
        val l = part[2].split("-").toTypedArray()
        val level = if (l.size > 1) (l[0].toInt()..l[1].toInt()).random() else l[0].toInt()
        val item: ItemStack?

        if (level != 0) {
            item = when (part[0].toLowerCase()) {
                "hand" -> player.inventory.itemInMainHand
                "offhand" -> player.inventory.itemInOffHand
                "helmet" -> player.inventory.armorContents[3]
                "chestplate" -> player.inventory.armorContents[2]
                "leggings" -> player.inventory.armorContents[1]
                "boots" -> player.inventory.armorContents[0]
                else -> player.inventory.getItem(NumberUtils.toInt(part[0], 0))
            }

            if (part[1].toLowerCase().startsWith("custom:") || part[1].toLowerCase().startsWith("c:")) {
                val loreConfig = part[1].split(":").toTypedArray()
                val lore = (loreConfig[1] + " " + convertLevelString(level)).replace("&", "§")
                val meta = item!!.itemMeta
                if (meta!!.hasLore()) {
                    val itemLore = meta.lore
                    val lineNumber = if (loreConfig.size > 2 && itemLore!!.size >= loreConfig[2].toInt()) loreConfig[2].toInt() else 0
                    itemLore?.add(lineNumber, lore)
                    meta.lore = itemLore
                } else {
                    meta.lore = listOf(lore)
                }
                item.itemMeta = meta
            }
            if (enchant != null) item!!.addUnsafeEnchantment(enchant, level)
        }
    }

    private fun convertLevelString(i: Int): String? {
        return when (i) {
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            6 -> "VI"
            7 -> "VII"
            8 -> "VIII"
            9 -> "IX"
            10 -> "X"
            else -> i.toString() + ""
        }
    }

}
