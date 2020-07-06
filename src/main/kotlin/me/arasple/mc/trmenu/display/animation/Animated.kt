package me.arasple.mc.trmenu.display.animation

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/6 16:26
 */
class Animated<T>(var elements: Array<T>) {

    constructor()

    private val id = AnimationHandler.nextId()

    fun nextElement(player: Player) = getElement(nextIndex(player))

    fun nextElement(player: Player, default: T) = getElement(nextIndex(player)) ?: default

    fun nextIndex(player: Player) = AnimationHandler.frame(player, id, elements.size)

    fun currentIndex(player: Player) = AnimationHandler.getIndex(player, id)

    private fun getElement(index: Int) =
        if (index > elements.size - 1) {
            null
        } else elements[index]


}