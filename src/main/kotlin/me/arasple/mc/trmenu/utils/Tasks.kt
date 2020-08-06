package me.arasple.mc.trmenu.utils

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * @author Arasple
 * @date 2020/2/27 9:30
 */
object Tasks {

    fun task(runnable: Runnable) = task(false, runnable)

    fun task(async: Boolean, runnable: Runnable) = if (async) Bukkit.getScheduler().runTaskAsynchronously(TrMenu.plugin, runnable) else Bukkit.getScheduler().runTask(TrMenu.plugin, runnable)

    fun delay(delay: Long, runnable: Runnable) = delay(delay, false, runnable)

    fun delay(delay: Long, async: Boolean, runnable: Runnable) = if (async) Bukkit.getScheduler().runTaskLaterAsynchronously(TrMenu.plugin, runnable, delay) else Bukkit.getScheduler().runTaskLater(TrMenu.plugin, runnable, delay)

    fun timer(delay: Long, period: Long, runnable: Runnable) = timer(delay, period, false, runnable)

    fun timer(delay: Long, period: Long, async: Boolean, runnable: Runnable) = if (async) Bukkit.getScheduler().runTaskTimerAsynchronously(TrMenu.plugin, runnable, delay, period) else Bukkit.getScheduler().runTaskTimer(TrMenu.plugin, runnable, delay, period)

    class Tasking(val tasks: MutableMap<UUID, MutableSet<BukkitTask>>) {

        constructor() : this(mutableMapOf())

        fun reset(player: Player) {
            try {
                tasks(player).removeIf {
                    it.cancel()
                    true
                }
            } catch (e: Throwable) {
            }
        }

        fun task(player: Player, vararg task: BukkitTask) = tasks(player).addAll(task)

        fun tasks(player: Player) = tasks.computeIfAbsent(player.uniqueId) { mutableSetOf() }

    }

}