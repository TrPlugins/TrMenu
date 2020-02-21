package me.arasple.mc.trmenu;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.MenuLoader;
import me.arasple.mc.trmenu.updater.Updater;
import me.arasple.mc.trmenu.utils.Bungees;
import me.arasple.mc.trmenu.utils.FileWatcher;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * @author Arasple
 * @date 2019/12/8 9:09
 */
public class TrMenuLoader {

    void init() {
        if (!TrMenu.SETTINGS.getBoolean("OPTIONS.SILENT", false)) {
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

        if (installDepend()) {
            return;
        }

        updateConfig();
        Updater.init(TrMenu.getPlugin());
        Bungees.init();
        MenuLoader.init();
        MenuLoader.loadMenus(Bukkit.getConsoleSender());
    }


    void active() {
        TLocale.sendToConsole("PLUGIN.ENABLED", TrMenu.getPlugin().getDescription().getVersion());
    }

    void cancel() {
        Bukkit.getOnlinePlayers().stream().filter(TrMenuAPI::isViewingMenu).forEach(HumanEntity::closeInventory);
        FileWatcher.getWatcher().unregisterAll();
        if (Updater.isAutoUpdate() && Updater.isOld()) {
            String url = "https://arasple.oss-cn-beijing.aliyuncs.com/files/TrMenu.jar";
            Files.downloadFile(url, TrMenu.getPluginFile());
            TrMenu.getPlugin().getLogger().log(Level.INFO, "Successfully downloaded a new version of TrMenu...");
        }
    }

    private boolean installDepend() {
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

    private void updateConfig() {
        TConfig oldCfg = TrMenu.SETTINGS;
        YamlConfiguration newCfg = YamlConfiguration.loadConfiguration(new InputStreamReader(TrMenu.getPlugin().getResource("settings.yml")));
        if (newCfg.getInt("CONFIG-VERSION") == TrMenu.SETTINGS.getInt("CONFIG-VERSION")) {
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

}
