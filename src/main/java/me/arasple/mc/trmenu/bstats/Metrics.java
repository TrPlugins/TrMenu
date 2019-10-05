package me.arasple.mc.trmenu.bstats;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trmenu.TrMenu;

import java.text.DecimalFormat;

/**
 * @author Arasple
 */
public class Metrics {

    private static MetricsBukkit metrics;
    private static DecimalFormat doubleFormat = new DecimalFormat("#.#");
    private static int openCount = 0;

    @TSchedule
    public static void init() {
        metrics = new MetricsBukkit(TrMenu.getPlugin());

        // 菜单打开次数统计
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("menu_open_counts", () -> {
            int i = openCount;
            openCount = 0;
            return i;
        }));
        // 选项 - 相似度比
        metrics.addCustomChart(new MetricsBukkit.SimplePie("option_material_similar_degree", () -> doubleFormat.format(TrMenu.getSettings().getDouble("OPTIONS.MATERIAL-SIMILAR-DEGREE", 0.8))));
        // 选项 - 相似度比
        metrics.addCustomChart(new MetricsBukkit.SimplePie("option_anti_click_spam", () -> String.valueOf(TrMenu.getSettings().getInt("OPTIONS.ANTI-CLICK-SPAM", 200))));
    }

    public static MetricsBukkit getMetrics() {
        return metrics;
    }

    public static void increaseOpenCount() {
        openCount++;
    }

}