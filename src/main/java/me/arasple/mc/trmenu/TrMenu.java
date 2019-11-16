package me.arasple.mc.trmenu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.trmenu.loader.MenuLoader;
import me.arasple.mc.trmenu.menu.Menur;
import me.arasple.mc.trmenu.menu.MenurHolder;
import me.arasple.mc.trmenu.updater.UpdateChecker;
import org.bukkit.Bukkit;

import java.util.List;

/**
 * @author Arasple
 * @since 2019.10.2
 * -
 * Doc: https://arasple.gitbook.io/trmenu
 */
@TrMenuPlugin.Version(5.11)
public final class TrMenu extends TrMenuPlugin {

    @TInject("§3Tr§bMenu")
    private static TLogger logger;
    @TInject("settings.yml")
    private static TConfig settings;
    private static List<Menur> menus = Lists.newArrayList();

    @Override
    public void onStarting() {
        UpdateChecker.check();
        TLocale.sendToConsole("PLUGIN.LOADING");
        MenuLoader.loadMenus(menus, Bukkit.getConsoleSender());
        TLocale.sendToConsole("PLUGIN.ENABLED", getDescription().getVersion());
    }

    @Override
    public void onStopping() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenurHolder) {
                player.closeInventory();
            }
        });

        TLocale.sendToConsole("PLUGIN.DISABLED");
    }

    /*
    GETTERS
     */

    public static List<Menur> getMenus() {
        return menus;
    }

    public static TLogger getTLogger() {
        return logger;
    }

    public static TConfig getSettings() {
        return settings;
    }


}
