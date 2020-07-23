package me.arasple.mc.trmenu.utils

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/23 19:33
 */
class Tasking(val tasks: MutableMap<UUID, MutableSet<BukkitTask>>) {

    constructor() : this(mutableMapOf())

    fun reset(player: Player) = tasks(player).removeIf {
        it.cancel()
        true
    }

    fun task(player: Player, task: BukkitTask) {
        tasks(player).add(task)
    }

    fun tasks(player: Player) = tasks.computeIfAbsent(player.uniqueId) { mutableSetOf() }

}