package me.arasple.mc.trmenu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.trmenu.inv.Menu;
import me.arasple.mc.trmenu.inv.MenuHolder;
import me.arasple.mc.trmenu.loader.MenuLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

/**
 * @author Arasple
 * @since 2019.10.2
 * -
 * Menu -> Buttons
 * -
 * Each button has unlimited icons display by condition,
 * and each icons support dyniamc display items as well as actions
 */
@TrMenuPlugin.Version(5.07)
public final class TrMenu extends TrMenuPlugin {

    @TInject("§3Tr§bMenu")
    private static TLogger logger;
    @TInject("settings.yml")
    private static TConfig settings;
    private static List<Menu> menus = Lists.newArrayList();

    @Override
    public void onStarting() {
        TLocale.sendToConsole("PLUGIN.LOADING");
        loadMenus(Bukkit.getConsoleSender());
        TLocale.sendToConsole("PLUGIN.ENABLED", getDescription().getVersion());
    }

    @Override
    public void onStopping() {
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
                player.closeInventory();
            }
        });

        TLocale.sendToConsole("PLUGIN.DISABLED");
    }

    /**
     * 加载菜单
     *
     * @param senders 接收反馈者
     */
    public static void loadMenus(CommandSender... senders) {
        menus.clear();

        File folder = new File(getPlugin().getDataFolder(), "menus");
        if (!folder.exists()) {
            getPlugin().saveResource("menus/example.yml", true);
        }

        long start = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            int allMenus = MenuLoader.countFiles(folder);
            List<String> errors = MenuLoader.loadMenu(folder);

            for (CommandSender sender : senders) {
                if (menus.size() > 0) {
                    TLocale.sendTo(sender, "MENU.LOADED-SUCCESS", menus.size(), System.currentTimeMillis() - start);
                }

                if (allMenus - menus.size() > 0 && !errors.isEmpty() && errors.size() > 0) {
                    TLocale.sendTo(sender, "MENU.LOADED-FAILURE", allMenus - menus.size());
                    getTLogger().warn("&6--------------------------------------------------");
                    getTLogger().error("");
                    errors.forEach(error -> sender.sendMessage("§8[§3Tr§bMenu§8]§8[§7INFO§8] §6" + error));
                    getTLogger().error("");
                    getTLogger().warn("&6--------------------------------------------------");
                }
            }
        });
    }

    public static List<Menu> getMenus() {
        return menus;
    }

    public static TLogger getTLogger() {
        return logger;
    }

    public static TConfig getSettings() {
        return settings;
    }

}
