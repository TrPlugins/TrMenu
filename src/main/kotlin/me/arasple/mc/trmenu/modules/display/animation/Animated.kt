package me.arasple.mc.trmenu.modules.display.animation

import me.arasple.mc.trmenu.modules.function.script.Scripts
import me.arasple.mc.trmenu.util.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/6 16:26
 */
data class Animated<T>(val condition: String?, var elements: Array<T>, var animatable: Boolean) {

    constructor(elements: Array<T>) : this(null, elements, elements.size > 1)

    private val id = AnimationHandler.nextId()

    fun currentElement(player: Player): T? {
        return getElement(currentIndex(player))
    }

    fun currentElement(player: Player, default: T): T {
        return currentElement(player) ?: default
    }

    fun nextElement(player: Player): T? {
        return getElement(nextIndex(player))
    }

    fun nextElement(player: Player, default: T): T {
        return nextElement(player) ?: default
    }

    fun nextIndex(player: Player): Int {
        if (condition.isNullOrBlank() || !Scripts.expression(player, condition).asBoolean()) {
            return if (animatable) AnimationHandler.frame(player, id, elements.size) else 0
        }
        return currentIndex(player)
    }

    fun currentIndex(player: Player): Int {
        return if (animatable) AnimationHandler.getIndex(player, id) else 0
    }

    @Suppress("UNCHECKED_CAST")
    fun addElement(element: T) {
        val size = elements.size
        elements = elements.copyOf(size + 1).let {
            it[size] = element
            return@let it as Array<T>
        }
        animatable = elements.size > 1
    }

    private fun getElement(index: Int) = if (index < 0 || index > elements.size - 1) null else elements[index]

    /*
    Generated
     */

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Animated<*>

        if (!elements.contentEquals(other.elements)) return false
        if (animatable != other.animatable) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = elements.contentHashCode()
        result = 31 * result + animatable.hashCode()
        result = 31 * result + id
        return result
    }

    fun reset(player: Player) = AnimationHandler.reset(player, id)

    fun isUpdatable(): Boolean {
        return animatable || elements.any {
            Msger.containsPlaceholders(it.toString())
        }
    }

    fun isEmpty(): Boolean = elements.isEmpty()

}