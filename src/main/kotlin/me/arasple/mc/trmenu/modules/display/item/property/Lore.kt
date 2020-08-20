package me.arasple.mc.trmenu.modules.display.item.property

import me.arasple.mc.trmenu.modules.conf.property.Nodes
import me.arasple.mc.trmenu.modules.display.item.Item
import me.arasple.mc.trmenu.modules.function.script.Scripts
import me.arasple.mc.trmenu.util.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 14:09
 */
class Lore(val lore: MutableList<String>, val conditions: MutableMap<Int, Pair<String, String>>) {

    constructor(lore: List<String>) : this(lore.toMutableList(), mutableMapOf())

    init {
        lore.forEachIndexed { index, lore ->
            Nodes.read(lore).let {
                it.second[Nodes.REQUIREMENT]?.let { condition ->
                    if (condition.isNotBlank()) conditions[index] = Pair(condition, Item.colorizeLore(it.first))
                }
            }
        }
    }

    fun isUpdateable(): Boolean {
        return lore.any {
            Msger.containsPlaceholders(it)
        }
    }

    fun formatedLore(player: Player): MutableList<String> {
        val lores = mutableListOf<String>()
        lore.forEachIndexed { index, lore ->
            val condition = conditions[index]
            if (condition != null) {
                if (Scripts.expression(player, condition.first).asBoolean()) lores.addAll(formatLores(player, condition.second))
                else lores.add("")
            } else lores.addAll(formatLores(player, Item.colorizeLore(lore)))
        }
        return lores
    }

    private fun formatLores(player: Player, string: String) = Msger.replace(player, string).split("\\n")

}