package me.arasple.mc.trmenu.modules.display.function

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/7 10:20
 */
data class Reactions(val reactions: List<Reaction>, val isEmpty: Boolean = reactions.isEmpty() || reactions.all { it.isEmpty }) {

    fun eval(player: Player): Boolean {
        if (isEmpty) return true
        reactions.sortedBy { it.priority }.forEach {
            if (!it.react(player)) return false
        }
        return true
    }

}