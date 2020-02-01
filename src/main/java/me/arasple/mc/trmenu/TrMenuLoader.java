package me.arasple.mc.trmenu;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trmenu.menu.MenuHolder;
import me.arasple.mc.trmenu.menu.MenuLoader;
import me.arasple.mc.trmenu.updater.Updater;
import me.arasple.mc.trmenu.utils.Bungees;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/12/8 9:09
 */
public class TrMenuLoader {

    void init() {
        if (!TrMenu.getSettings().getBoolean("OPTIONS.SILENT", false)) {
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
        }
        TLocale.sendToConsole("PLUGIN.LOADING");

        if (hookPlaceholderAPI()) {
            return;
        }


        updateConfig();
        Updater.init(TrMenu.getPlugin());
        Bungees.init();
        MenuLoader.init();
        MenuLoader.loadMenus(Bukkit.getConsoleSender());
    }

    private void updateConfig() {
        TConfig oldCfg = TrMenu.getSettings();
        YamlConfiguration newCfg = YamlConfiguration.loadConfiguration(new InputStreamReader(TrMenu.getPlugin().getResource("settings.yml")));
        if (newCfg.getInt("CONFIG-VERSION") == TrMenu.getSettings().getInt("CONFIG-VERSION")) {
            return;
        }
        oldCfg.set("CONFIG-VERSION", newCfg.getInt("CONFIG-VERSION"));
        oldCfg.getKeys(true).forEach(key -> {
            if (!newCfg.contains(key)) {
                oldCfg.set(key, null);
            }
        });
        newCfg.getKeys(true).forEach(key -> {
            if (!oldCfg.contains(key)) {
                oldCfg.set(key, newCfg.get(key));
            }
        });
        oldCfg.saveToFile();
    }


    void load() {
        TLocale.sendToConsole("PLUGIN.ENABLED", TrMenu.getPlugin().getDescription().getVersion());
    }

    public static void unload() {
        Bukkit.getOnlinePlayers().stream().filter(p -> p.getOpenInventory().getTopInventory() instanceof MenuHolder).collect(Collectors.toList()).forEach(HumanEntity::closeInventory);
        if (TrMenu.getSettings().getBoolean("OPTIONS.AUTO-UPDATE", false) && Updater.isOld()) {
            String url = "https://arasple.oss-cn-beijing.aliyuncs.com/files/TrMenu.jar";
            TLocale.sendToConsole("PLUGIN.UPDATER.DOWNLOADING", Updater.getNewVersion());
            if (Files.downloadFile(url, TrMenu.getPluginFile())) {
                TLocale.sendToConsole("PLUGIN.UPDATER.DOWNLOAD-COMPLETED");
                return;
            }
            TLocale.sendToConsole("PLUGIN.UPDATER.DOWNLOAD-FAILED");
        }
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
            } else {
                TLocale.sendToConsole("PLUGIN.DEPEND.INSTALL-FAILED", "PlaceholderAPI");
            }
            Bukkit.shutdown();
            return true;
        }
        return false;
    }

}
