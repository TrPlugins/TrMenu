package me.arasple.mc.trmenu.module.internal.service

import io.izzel.taboolib.module.inject.TFunction
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.module.display.Menu
import org.bstats.bukkit.Metrics
import org.bukkit.event.inventory.InventoryType


/**
 * @author Arasple
 * @date 2020/3/7 22:15
 */
object Metrics {

    private val B_STATS: Metrics by lazy { Metrics(TrMenu.plugin, 5742) }
    var menuOpenCounts = 0
        get() {
            val count = field
            field = 0
            return count
        }


    @TFunction.Init
    fun initialization() {
        B_STATS.let { metrics ->
            metrics.addCustomChart(Metrics.SingleLineChart("menus") {
                Menu.menus.sumBy { menu -> menu.layout.getSize() }
            })
            metrics.addCustomChart(Metrics.SingleLineChart("menu_open_counts") {
                menuOpenCounts
            })

            metrics.addCustomChart(Metrics.AdvancedPie("menu_size") {
                val value = mutableMapOf<String, Int>()

                Menu.menus
                    .flatMap {
                        it.layout.layouts.filter { it.type == InventoryType.CHEST }
                    }.forEach {
                        value[it.rows.toString()] = value.computeIfAbsent(it.rows.toString()) { 0 } + 1
                    }

                value
            })

            metrics.addCustomChart(Metrics.AdvancedPie("item_texture") {
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

            metrics.addCustomChart(Metrics.AdvancedPie("inventory_type") {
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