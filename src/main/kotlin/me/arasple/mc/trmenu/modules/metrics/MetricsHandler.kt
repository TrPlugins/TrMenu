package me.arasple.mc.trmenu.modules.metrics

import io.izzel.taboolib.metrics.CStats
import io.izzel.taboolib.module.inject.TSchedule
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.display.Menu
import org.bstats.bukkit.Metrics

/**
 * @author Arasple
 * @date 2020/3/7 22:15
 */
object MetricsHandler {

    var B_STATS: Metrics? = null
    var C_STATS: CStats? = null
    private val counts = intArrayOf(0, 0)

    @TSchedule
    fun init() {
        B_STATS = Metrics(TrMenu.plugin, 5742)
        C_STATS = CStats(TrMenu.plugin)

        registerCharts()
    }

    private fun registerCharts() {
        B_STATS?.let {
            it.addCustomChart(Metrics.SingleLineChart("menus") { Menu.getMenus().sumBy { menu -> menu.layout.layouts.size } })
            it.addCustomChart(Metrics.SingleLineChart("menu_open_counts") {
                val i = counts[0]
                counts[0] = 0
                i
            })
            it.addCustomChart(Metrics.SingleLineChart("action_run_counts") {
                val i = counts[1]
                counts[1] = 0
                i
            })
        }
    }

    fun increase(index: Int) {
        counts[index]++
    }

}