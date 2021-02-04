package me.arasple.mc.trmenu.util

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

/**
 * @author Arasple
 * @date 2020/2/27 9:30
 */
object Tasks {

    private val plugin = TrMenu.plugin
    private val scheduler = Bukkit.getScheduler()

    fun task(async: Boolean = !Bukkit.isPrimaryThread(), runnable: () -> Unit): BukkitTask {
        return if (async) scheduler.runTaskAsynchronously(plugin, runnable)
        else scheduler.runTask(plugin, runnable)
    }

    fun delay(delay: Long = 2L, async: Boolean = !Bukkit.isPrimaryThread(), runnable: () -> Unit): BukkitTask {
        return if (async) scheduler.runTaskLaterAsynchronously(plugin, runnable, delay)
        else scheduler.runTaskLater(plugin, runnable, delay)
    }

    fun timer(delay: Long = 2L, period: Long = 20L, async: Boolean = !Bukkit.isPrimaryThread(), runnable: () -> Unit): BukkitTask {
        return if (async) scheduler.runTaskTimerAsynchronously(plugin, runnable, delay, period)
        else scheduler.runTaskTimer(plugin, runnable, delay, period)
    }

}