package me.arasple.mc.trmenu;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trmenu.menu.MenuHolder;
import me.arasple.mc.trmenu.menu.MenuLoader;
import me.arasple.mc.trmenu.updater.Updater;
import me.arasple.mc.trmenu.utils.Bungees;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/12/8 9:09
 */
public class TrMenuLoader {

    void init() {
        Arrays.asList(
                "",
                "§3  ____________________   _____  __________________   ____ ___",
                "§3\\__    ___\\______   \\ /     \\ \\_   _____/\\      \\ |    |   \\",
                "§3  |    |   |       _//  \\ /  \\ |    __)_ /   |   \\|    |   /",
                "§3  |    |   |    |   /    Y    \\|        /    |    |    |  /",
                "§3  |____|   |____|_  \\____|__  /_______  \\____|__  |______/",
                "§3                  \\/        \\/        \\/        \\/"
        )
                .forEach(l -> Bukkit.getConsoleSender().sendMessage(l));
        TLocale.sendToConsole("PLUGIN.LOADING");

        if (hookPlaceholderAPI()) {
            return;
        }

        Updater.init(TrMenu.getPlugin());
        Bungees.init();
        MenuLoader.init();
        MenuLoader.loadMenus(Bukkit.getConsoleSender());
    }


    void load() {
        TLocale.sendToConsole("PLUGIN.ENABLED", TrMenu.getPlugin().getDescription().getVersion());
    }

    void unload() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
                player.closeInventory();
            }
        });

        TLocale.sendToConsole("PLUGIN.DISABLED");
    }


    /**
     * 检测前置 PlaceholderAPI
     * 并自动下载、重启服务器
     */
    private boolean hookPlaceholderAPI() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        File jarFile = new File("plugins/PlaceholderAPI.jar");
        String url = "https://api.spiget.org/v2/resources/6245/download";

        if (plugin == null) {
            jarFile.delete();
            TLocale.sendToConsole("PLUGIN.DEPEND.DOWNLOAD", "PlaceholderAPI");
            if (Files.downloadFile(url, jarFile)) {
                TLocale.sendToConsole("PLUGIN.DEPEND.INSTALL", "PlaceholderAPI");
                Bukkit.shutdown();
            } else {
                TLocale.sendToConsole("PLUGIN.DEPEND.INSTALL-FAILED", "PlaceholderAPI");
                Bukkit.shutdown();
            }
            return true;
        }
        return false;
    }

}
