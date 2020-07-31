package me.arasple.mc.trmenu.display.function

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/7 10:20
 */
data class Reactions(val reactions: List<Reaction>) {

    fun eval(player: Player): Boolean {
        if (isEmpty()) return true
        reactions.sortedBy { it.priority }.forEach {
            if (!it.react(player)) return false
        }
        return true
    }

    fun isEmpty() = reactions.isEmpty()

}