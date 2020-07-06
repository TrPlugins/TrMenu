package me.arasple.mc.trmenu.utils

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2020/2/27 9:30
 */
object Tasks {

	fun runTask(runnable: Runnable) = runTask(runnable, false)

	fun runTask(runnable: Runnable, async: Boolean) = if (async) Bukkit.getScheduler().runTaskAsynchronously(TrMenu.plugin, runnable) else Bukkit.getScheduler().runTask(TrMenu.plugin, runnable)

	fun runDelayTask(runnable: Runnable, delay: Long) = runDelayTask(runnable, delay, false)

	fun runDelayTask(runnable: Runnable, delay: Long, async: Boolean) = if (async) Bukkit.getScheduler().runTaskLaterAsynchronously(TrMenu.plugin, runnable, delay) else Bukkit.getScheduler().runTaskLater(TrMenu.plugin, runnable, delay)

	fun runTimerTask(runnable: Runnable, delay: Long, period: Long) = runTimerTask(runnable, delay, period, false)

	fun runTimerTask(runnable: Runnable, delay: Long, period: Long, async: Boolean) = if (async) Bukkit.getScheduler().runTaskTimerAsynchronously(TrMenu.plugin, runnable, delay, period) else Bukkit.getScheduler().runTaskTimer(TrMenu.plugin, runnable, delay, period)

}