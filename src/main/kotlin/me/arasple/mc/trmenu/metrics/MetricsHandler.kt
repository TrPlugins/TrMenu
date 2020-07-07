package me.arasple.mc.trmenu.metrics

import io.izzel.taboolib.metrics.CStats
import io.izzel.taboolib.module.inject.TSchedule
import me.arasple.mc.trmenu.TrMenu
import org.bstats.bukkit.Metrics

/**
 * @author Arasple
 * @date 2020/3/7 22:15
 */
object MetricsHandler {

    var B_STATS: Metrics? = null
    var C_STATS: CStats? = null

    @TSchedule
    fun init() {
        B_STATS = Metrics(TrMenu.plugin, 5742)
        C_STATS = CStats(TrMenu.plugin)
    }

}