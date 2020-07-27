package me.arasple.mc.trmenu.display.position

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/27 16:56
 */
data class Position(val staticSlots: Set<Int>, val dynamicSlots: Set<String>, val currentDynamic: MutableSet<Int>) {

    constructor(slots: Set<Int>) : this(slots, setOf(), mutableSetOf())

    fun getSlots(player: Player) = mutableSetOf<Int>().let { set ->
        set.addAll(staticSlots)
        currentDynamic.clear()
        dynamicSlots.forEach {
            val slot = NumberUtils.toInt(Msger.replace(player, it), -1)
            if (slot >= 0) {
                set.add(slot)
                currentDynamic.add(slot)
            }
        }
        return@let set
    }

    fun getOccupiedSlots(player: Player) = mutableSetOf<Int>().let {
        it.addAll(staticSlots)
        it.addAll(currentDynamic)
        it
    }

    companion object {

        fun createPosition(list: List<Any>): Position {
            val staticSlots = mutableSetOf<Int>()
            val dynamicSlots = mutableSetOf<String>()
            list.forEach {
                val num = it.toString()
                if (NumberUtils.isCreatable(num)) staticSlots.add(NumberUtils.toInt(num)) else dynamicSlots.add(num)
            }
            return Position(staticSlots, dynamicSlots, mutableSetOf())
        }

    }

}