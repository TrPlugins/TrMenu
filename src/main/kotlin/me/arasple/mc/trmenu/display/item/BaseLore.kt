package me.arasple.mc.trmenu.display.item

import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Nodes
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 14:09
 */
class BaseLore(val lore: MutableList<String>, val conditions: MutableMap<Int, Pair<String, String>>) {

    init {
        lore.forEachIndexed { index, lore ->
            Nodes.read(lore).let { it ->
                it.second[Nodes.REQUIREMENT]?.let { condition ->
                    if (condition.isNotBlank()) conditions[index] = Pair(condition, it.first)
                }
            }
        }
    }

    fun formatedLore(player: Player): List<String> {
        val lores = mutableListOf<String>()
        lore.forEachIndexed { index, lore ->
            val condition = conditions[index]
            if (condition != null) {
                lores.add(if (Scripts.expression(player, condition.first).asBoolean()) Item.colorizeLore(condition.second) else "")
            } else lores.add(Item.colorizeLore(lore))
        }

        return Msger.replace(player, lores)
    }


}