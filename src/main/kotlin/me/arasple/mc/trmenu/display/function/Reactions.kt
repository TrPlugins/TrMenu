package me.arasple.mc.trmenu.display.function

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/7 10:20
 */
data class Reactions(val reactions: List<Reaction>) {

    fun eval(player: Player): Boolean {
        if (!isNotEmpty()) return false
        reactions.sortedBy { it.priority }.forEach {
            if (!it.react(player)) return false
        }
        return true
    }

    fun isNotEmpty(): Boolean = reactions.isNotEmpty()

}