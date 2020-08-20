package me.arasple.mc.trmenu.modules.display.animation

import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/5/30 14:15
 */
object AnimationHandler {

    private var id = 0
    private val indexs = mutableMapOf<UUID, MutableMap<Int, Int>>()

    fun frame(player: Player, id: Int, size: Int): Int {
        if (size == 0) return -1
        val index = getIndex(player)
        if (index.computeIfAbsent(id) { 0 } >= size - 1) index[id] = 0
        else index[id] = (index[id] ?: 0) + 1
        return index[id] ?: 0
    }

    fun getIndex(player: Player, id: Int) = getIndex(player)[id] ?: 0

    fun getIndex(player: Player) = indexs.computeIfAbsent(player.uniqueId) { mutableMapOf() }

    fun reset(player: Player) = indexs[player.uniqueId]?.clear()

    fun reset(player: Player, id: Int) = getIndex(player).remove(id)

    fun nextId() = id++

}