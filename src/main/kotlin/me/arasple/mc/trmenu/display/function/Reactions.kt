package me.arasple.mc.trmenu.display.function

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/7 10:20
 */
class Reactions(val reactions: List<Reaction>) {

    fun eval(player: Player) = reactions.sortedBy { it.priority }.forEach { if (!it.react(player)) return }

    fun isNotEmpty(): Boolean = reactions.isNotEmpty()

}