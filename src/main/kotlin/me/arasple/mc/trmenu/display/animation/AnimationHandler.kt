package me.arasple.mc.trmenu.display.animation

import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/5/30 14:15
 */
object AnimationHandler {

    private val indexs = mutableMapOf<UUID, MutableMap<String, Int>>()

    fun frame(player: Player, key: String, size: Int): Int {
        val index = indexs.computeIfAbsent(player.uniqueId) { mutableMapOf() }
        if (index.computeIfAbsent(key) { 0 } >= size - 1) index[key] = 0
        else index[key] = index[key]!! + 1
        return index[key]!!
    }

    fun reset(player: Player) {
        indexs[player.uniqueId]?.clear()
    }

}