package me.arasple.mc.trmenu.display.animation

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/6 16:26
 */
data class Animated<T>(var elements: Array<T>) {

    private val id = AnimationHandler.nextId()

    fun currentElement(player: Player) = getElement(currentIndex(player))

    fun currentElement(player: Player, default: T) = getElement(currentIndex(player)) ?: default

    fun nextElement(player: Player) = getElement(nextIndex(player))

    fun nextElement(player: Player, default: T) = getElement(nextIndex(player)) ?: default

    fun nextIndex(player: Player) = AnimationHandler.frame(player, id, elements.size)

    fun currentIndex(player: Player) = AnimationHandler.getIndex(player, id)

    @Suppress("UNCHECKED_CAST")
    fun addElement(element: T) {
        val size = elements.size
        elements = elements.copyOf(size + 1).let {
            it[size] = element
            return@let it as Array<T>
        }
    }

    private fun getElement(index: Int) = if (index > elements.size - 1) {
        null
    } else elements[index]


    /*
    Generated
     */

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Animated<*>

        if (!elements.contentEquals(other.elements)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = elements.contentHashCode()
        result = 31 * result + id
        return result
    }

}