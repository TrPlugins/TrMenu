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
        metrics.addCustomChart(new Metrics.SingleLineChart("menu_share_counts", () -> {
            int i = coutns[2];
            coutns[2] = 0;
            return i;
        }));
        // 统计菜单行数
        metrics.addCustomChart(new Metrics.AdvancedPie("menu_size", () -> {
            Map<String, Integer> data = new HashMap<>();
            TrMenu.getMenus().forEach(menu -> menu.getRows().values().forEach(rows -> {
                data.put(String.valueOf(rows), data.getOrDefault(rows, 0) + 1);
            }));
            return data;
        }));
        // 统计材质类型
        metrics.addCustomChart(new Metrics.AdvancedPie("menu_items", () -> {
            Map<String, Integer> data = new HashMap<>();
            TrMenu.getMenus().forEach(m -> m.getButtons().keySet().forEach(b -> {
                b.getDefIcon().getItem().getMaterials().forEach(mat -> {
                    String option = mat.getOption().name();
                    data.put(option, data.getOrDefault(option, 0) + 1);
                });
                b.getIcons().forEach(icon -> icon.getItem().getMaterials().forEach(mat -> {
                    String option = mat.getOption().name();
                    data.put(option, data.getOrDefault(option, 0) + 1);
                }));
            }));
            return data;
        }));
        // 统计容器类型
        metrics.addCustomChart(new Metrics.AdvancedPie("inventory_type", () -> {
            Map<String, Integer> data = new HashMap<>();
            TrMenu.getMenus().forEach(menu -> {
                InventoryType type = menu.getType();
                type = type == null ? InventoryType.CHEST : type;
                data.put(type.name(), data.getOrDefault(type.name(), 0) + 1);
            });
            return data;
        }));
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