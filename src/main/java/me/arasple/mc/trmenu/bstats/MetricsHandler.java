package me.arasple.mc.trmenu.bstats;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.hook.HookHeadDatabase;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.inventory.InventoryType;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arasple
 * @url https://bstats.org/plugin/bukkit/TrMenu
 */
public class MetricsHandler {

    private static Metrics metrics;
    private static DecimalFormat doubleFormat = new DecimalFormat("#.#");
    private static int[] coutns = new int[]{0, 0, 0};

    private static Map<String, Integer> MENU_SIZE, MENU_ITEMS, INVENTORY_TYPES;

    @TSchedule
    public static void init() {
        metrics = new Metrics(TrMenu.getPlugin(), 5742);
        // 菜单总数统计
        metrics.addCustomChart(new Metrics.SingleLineChart("menus", () -> TrMenu.getMenus().stream().mapToInt(menu -> menu.getRows().keySet().size()).sum()));
        // 菜单打开次数统计
        metrics.addCustomChart(new Metrics.SingleLineChart("menu_open_counts", () -> {
            int i = coutns[0];
            coutns[0] = 0;
            return i;
        }));
        // 动作次数
        metrics.addCustomChart(new Metrics.SingleLineChart("action_run_counts", () -> {
            int i = coutns[1];
            coutns[1] = 0;
            return i;
        }));
        // 菜單分享次數
        metrics.addCustomChart(new Metrics.SingleLineChart("menu_share_counts", () -> {
            int i = coutns[2];
            coutns[2] = 0;
            return i;
        }));
        // 统计菜单行数
        metrics.addCustomChart(new Metrics.AdvancedPie("menu_size", () -> MENU_SIZE));
        // 统计材质类型
        metrics.addCustomChart(new Metrics.AdvancedPie("menu_items", () -> MENU_ITEMS));
        // 统计容器类型
        metrics.addCustomChart(new Metrics.AdvancedPie("inventory_type", () -> INVENTORY_TYPES));
        // 选项 - 自动更新
        metrics.addCustomChart(new Metrics.SimplePie("option_auto_updater", () -> TrMenu.getSettings().getBoolean("OPTIONS.AUTO-UPDATE", false) ? "Enabled" : "Disabled"));
        // 选项 - 相似度比
        metrics.addCustomChart(new Metrics.SimplePie("option_material_similar_degree", () -> doubleFormat.format(TrMenu.getSettings().getDouble("OPTIONS.MATERIAL-SIMILAR-DEGREE", 0.8))));
        // 选项 - 防刷屏
        metrics.addCustomChart(new Metrics.SimplePie("option_anti_click_spam", () -> String.valueOf(TrMenu.getSettings().getInt("OPTIONS.ANTI-CLICK-SPAM", 200))));
        // 选项 - 菜单重载监听
        metrics.addCustomChart(new Metrics.SimplePie("menu_file_listener", () -> TrMenu.getSettings().getBoolean("OPTIONS.MENU-FILE-LISTENER.ENABLE", true) ? "Enabled" : "Disabled"));
        // 支持 - HeadDatabase
        metrics.addCustomChart(new Metrics.SimplePie("hooked_headdatabase", () -> HookHeadDatabase.isHoooked() ? "Enabled" : "Disabled"));
        // 初始化
        MENU_SIZE = new HashMap<>();
        MENU_ITEMS = new HashMap<>();
        INVENTORY_TYPES = new HashMap<>();
    }

    @TSchedule(delay = 20 * 60, period = 20 * 30 * 60)
    public static void reloadStatiscs() {
        MENU_SIZE.clear();
        MENU_ITEMS.clear();
        INVENTORY_TYPES.clear();

        TrMenu.getMenus().forEach(menu -> {
            menu.getRows().values().forEach(rows -> MENU_SIZE.put(String.valueOf(rows), MENU_SIZE.getOrDefault(rows, 0) + 1));
            menu.getButtons().keySet().forEach(button -> {
                button.getDefIcon().getItem().getMaterials().forEach(mat -> {
                    String option = mat.getOption().name();
                    MENU_ITEMS.put(option, MENU_ITEMS.getOrDefault(option, 0) + 1);
                });
                button.getIcons().forEach(icon -> icon.getItem().getMaterials().forEach(mat -> {
                    String option = mat.getOption().name();
                    MENU_ITEMS.put(option, MENU_ITEMS.getOrDefault(option, 0) + 1);
                }));
                InventoryType type = menu.getType();
                type = type == null ? InventoryType.CHEST : type;
                INVENTORY_TYPES.put(type.name(), INVENTORY_TYPES.getOrDefault(type.name(), 0) + 1);
            });
        });
    }

    public static void increase(int index) {
        if (coutns[index] < Integer.MAX_VALUE) {
            coutns[index] += 1;
        }
    }

    public static Metrics getMetrics() {
        return metrics;
    }

}