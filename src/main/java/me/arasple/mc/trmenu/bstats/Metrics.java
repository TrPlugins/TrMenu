package me.arasple.mc.trmenu.bstats;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.hook.HookHeadDatabase;
import org.bukkit.event.inventory.InventoryType;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arasple
 * @url https://bstats.org/plugin/bukkit/TrMenu
 */
public class Metrics {

    private static MetricsBukkit metrics;
    private static DecimalFormat doubleFormat = new DecimalFormat("#.#");
    private static int[] coutns = new int[]{0, 0};

    @TSchedule
    public static void init() {
        metrics = new MetricsBukkit(TrMenu.getPlugin());
        // 菜单总数统计
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("menus", () -> TrMenu.getMenus().size()));
        // 菜单打开次数统计 0
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("menu_open_counts", () -> {
            int i = coutns[0];
            coutns[0] = 0;
            return i;
        }));
        // 动作次数
        metrics.addCustomChart(new MetricsBukkit.SingleLineChart("action_run_counts", () -> {
            int i = coutns[1];
            coutns[1] = 0;
            return i;
        }));
        // 统计菜单行数
        metrics.addCustomChart(new MetricsBukkit.AdvancedPie("menu_size", () -> {
            Map<String, Integer> data = new HashMap<>();
            data.put("1", (int) TrMenu.getMenus().stream().filter(menur -> menur.getRows() == 1).count());
            data.put("2", (int) TrMenu.getMenus().stream().filter(menur -> menur.getRows() == 2).count());
            data.put("3", (int) TrMenu.getMenus().stream().filter(menur -> menur.getRows() == 3).count());
            data.put("4", (int) TrMenu.getMenus().stream().filter(menur -> menur.getRows() == 4).count());
            data.put("5", (int) TrMenu.getMenus().stream().filter(menur -> menur.getRows() == 5).count());
            data.put("6", (int) TrMenu.getMenus().stream().filter(menur -> menur.getRows() == 6).count());
            data.entrySet().removeIf(entry -> entry.getValue() <= 0);
            return data;
        }));
        // 统计容器类型
        metrics.addCustomChart(new MetricsBukkit.AdvancedPie("inventory_type", () -> {
            Map<String, Integer> data = new HashMap<>();
            for (InventoryType type : InventoryType.values()) {
                data.put(type.name(), (int) TrMenu.getMenus().stream().filter(menur -> menur.getInventoryType() == type).count());
            }
            data.entrySet().removeIf(entry -> entry.getValue() <= 0);
            return data;
        }));
        // 选项 - 相似度比
        metrics.addCustomChart(new MetricsBukkit.SimplePie("option_material_similar_degree", () -> doubleFormat.format(TrMenu.getSettings().getDouble("OPTIONS.MATERIAL-SIMILAR-DEGREE", 0.8))));
        // 选项 - 防刷屏
        metrics.addCustomChart(new MetricsBukkit.SimplePie("option_anti_click_spam", () -> String.valueOf(TrMenu.getSettings().getInt("OPTIONS.ANTI-CLICK-SPAM", 200))));
        // 选项 - 菜单重载监听
        metrics.addCustomChart(new MetricsBukkit.SimplePie("menu_file_listener", () -> TrMenu.getSettings().getBoolean("OPTIONS.MENU-FILE-LISTENER.ENABLE", true) ? "Enabled" : "Disabled"));
        // 支持 - HeadDatabase
        metrics.addCustomChart(new MetricsBukkit.SimplePie("hooked_headdatabase", () -> HookHeadDatabase.isHoooked() ? "Enabled" : "Disabled"));
    }

    public static void increase(int index) {
        increase(index, 1);
    }

    public static void increase(int index, int value) {
        if (coutns[index] < Integer.MAX_VALUE) {
            coutns[index] += value;
        }
    }

    public static MetricsBukkit getMetrics() {
        return metrics;
    }

}