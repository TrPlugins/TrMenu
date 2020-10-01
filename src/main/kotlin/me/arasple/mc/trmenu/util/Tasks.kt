package me.arasple.mc.trmenu.util

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

    private val plugin = TrMenu.plugin
    private val scheduler = Bukkit.getScheduler()

    fun task(runnable: Runnable) {
        task(false, runnable)
    }

    fun task(async: Boolean, runnable: Runnable) {
        if (async) {
            scheduler.runTaskAsynchronously(plugin, runnable)
        } else {
            scheduler.runTask(plugin, runnable)
        }
    }

    fun delay(delay: Long, runnable: Runnable) {
        delay(delay, false, runnable)
    }

    fun delay(delay: Long, async: Boolean, runnable: Runnable) {
        if (async) {
            scheduler.runTaskLaterAsynchronously(plugin, runnable, delay)
        } else {
            scheduler.runTaskLater(plugin, runnable, delay)
        }
    }

    fun timer(delay: Long, period: Long, runnable: Runnable) {
        timer(delay, period, false, runnable)
    }

    fun timer(delay: Long, period: Long, async: Boolean, runnable: Runnable) {
        if (async) {
            scheduler.runTaskTimerAsynchronously(plugin, runnable, delay, period)
        } else {
            scheduler.runTaskTimer(plugin, runnable, delay, period)
        }
    }

    class Tasking(val tasks: MutableMap<UUID, MutableSet<BukkitTask>>) {

        constructor() : this(mutableMapOf())

        fun reset(player: Player) {
            try {
                tasks(player).removeIf {
                    it.cancel()
                    true
                }
            } catch (e: Throwable) {
                println("TrMenu Error: ${e.message}")
            }
        }

        fun task(player: Player, vararg task: BukkitTask) = tasks(player).addAll(task)

        fun tasks(player: Player) = tasks.computeIfAbsent(player.uniqueId) { mutableSetOf() }

    }

}