package trplugins.menu.module.internal.service

import trplugins.menu.module.display.Menu
import org.bukkit.event.inventory.InventoryType
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.pluginVersion
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.*

/**
 * @author Arasple
 * @date 2020/3/7 22:15
 */
object Metrics {

    private val B_STATS: Metrics by lazy { Metrics(5742, pluginVersion, Platform.BUKKIT) }
    var menuOpenCounts = 0
        get() {
            val count = field
            field = 0
            return count
        }



    @Awake(LifeCycle.INIT)
    fun initialization() {
        B_STATS.let { metrics ->
            metrics.addCustomChart(SingleLineChart("menus") {
                Menu.menus.sumOf { menu -> menu.layout.getSize() }
            })
            metrics.addCustomChart(SingleLineChart("menu_open_counts") {
                menuOpenCounts
            })

            metrics.addCustomChart(AdvancedPie("menu_size") {
                val value = mutableMapOf<String, Int>()

                Menu.menus
                    .flatMap {
                        it.layout.layouts.filter { it.type == InventoryType.CHEST }
                    }.forEach {
                        value[it.rows.toString()] = value.computeIfAbsent(it.rows.toString()) { 0 } + 1
                    }

                value
            })

            metrics.addCustomChart(AdvancedPie("item_texture") {
                val value = mutableMapOf<String, Int>()

                Menu.menus
                    .flatMap {
                        val list = it.icons.map { icon -> icon.defIcon.display }.toMutableList()
                        list.addAll(it.icons.flatMap { icon -> icon.subs.elements.map { sub -> sub.display } })
                        list.flatMap { it.texture.elements }
                    }.forEach {
                        value[it.type.name] = value.computeIfAbsent(it.type.name) { 0 } + 1
                    }

                value
            })

            metrics.addCustomChart(AdvancedPie("inventory_type") {
                val value = mutableMapOf<String, Int>()

                Menu.menus
                    .flatMap {
                        it.layout.layouts.toList()
                    }.forEach {
                        value[it.type.name] = value.computeIfAbsent(it.type.name) { 0 } + 1
                    }

                value
            })
        }
    }

}