package me.arasple.mc.trmenu.display.item

import me.arasple.mc.trmenu.configuration.property.Nodes
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
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
                    if (condition.isNotBlank()) conditions[index] = Pair(condition, it.first)
                }
            }
        }
    }

    fun formatedLore(player: Player): List<String> {
        val collect = mutableListOf<String>()
        val lores = mutableListOf<String>()
        lore.forEachIndexed { index, lore ->
            val condition = conditions[index]
            if (condition != null) {
                collect.add(if (Scripts.expression(player, condition.first).asBoolean()) Item.colorizeLore(condition.second) else "")
            } else collect.add(Item.colorizeLore(lore))
        }
        collect.forEach {
            it.split("\\n").forEach { part ->
                val msg = Msger.replace(player, part)
                if (part.contains("%")) {
                    println("[$part -> | $msg]")
                }
                lores.add(msg)
            }
        }
        return lores
    }


}