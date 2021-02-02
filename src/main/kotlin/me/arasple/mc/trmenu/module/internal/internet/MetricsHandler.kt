package me.arasple.mc.trmenu.module.internal.internet

import io.izzel.taboolib.module.inject.TFunction
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.module.display.Menu
import org.bstats.bukkit.Metrics

/**
 * @author Arasple
 * @date 2020/3/7 22:15
 */
object MetricsHandler {

    private val B_STATS: Metrics by lazy { Metrics(TrMenu.plugin, 5742) }
    var menuOpenCounts = 0
        get() {
            val count = field
            field = 0
            return count
        }


    @TFunction.Init
    fun initialization() {
        B_STATS.let {
            it.addCustomChart(Metrics.SingleLineChart("menus") {
                Menu.menus.sumBy { menu -> menu.layout.getSize() }
            })
            it.addCustomChart(Metrics.SingleLineChart("menu_open_counts") {
                menuOpenCounts
            })
            // TODO
        }
    }

}