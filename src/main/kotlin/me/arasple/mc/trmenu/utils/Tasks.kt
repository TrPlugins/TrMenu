package me.arasple.mc.trmenu.utils

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2020/2/27 9:30
 */
object Tasks {

    fun runTask(runnable: Runnable) = runTask(false, runnable)

    fun runTask(async: Boolean, runnable: Runnable) = if (async) Bukkit.getScheduler().runTaskAsynchronously(TrMenu.plugin, runnable) else Bukkit.getScheduler().runTask(TrMenu.plugin, runnable)

    fun runDelayTask(delay: Long, runnable: Runnable) = runDelayTask(delay, false, runnable)

    fun runDelayTask(delay: Long, async: Boolean, runnable: Runnable) = if (async) Bukkit.getScheduler().runTaskLaterAsynchronously(TrMenu.plugin, runnable, delay) else Bukkit.getScheduler().runTaskLater(TrMenu.plugin, runnable, delay)

    fun runTimerTask(delay: Long, period: Long, runnable: Runnable) = runTimerTask(delay, period, false, runnable)

    fun runTimerTask(delay: Long, period: Long, async: Boolean, runnable: Runnable) = if (async) Bukkit.getScheduler().runTaskTimerAsynchronously(TrMenu.plugin, runnable, delay, period) else Bukkit.getScheduler().runTaskTimer(TrMenu.plugin, runnable, delay, period)

}