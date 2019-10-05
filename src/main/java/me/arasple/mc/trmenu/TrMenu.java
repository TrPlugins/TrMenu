package me.arasple.mc.trmenu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.trmenu.inv.Menur;
import me.arasple.mc.trmenu.inv.MenurHolder;
import me.arasple.mc.trmenu.loader.MenuLoader;
import org.bukkit.Bukkit;

import java.util.List;

/**
 * @author Arasple
 * @since 2019.10.2
 */
@TrMenuPlugin.Version(5.07)
public final class TrMenu extends TrMenuPlugin {

    @TInject("§3Tr§bMenu")
    private static TLogger logger;
    @TInject("settings.yml")
    private static TConfig settings;
    private static List<Menur> menus = Lists.newArrayList();

    public static List<Menur> getMenus() {
        return menus;
    }

    public static TLogger getTLogger() {
        return logger;
    }

    public static TConfig getSettings() {
        return settings;
    }

    @Override
    public void onStarting() {
        TLocale.sendToConsole("PLUGIN.LOADING");
        MenuLoader.loadMenus(menus, Bukkit.getConsoleSender());
        TLocale.sendToConsole("PLUGIN.ENABLED", getDescription().getVersion());
    }

    @Override
    public void onStopping() {
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenurHolder) {
                player.closeInventory();
            }
        });

        TLocale.sendToConsole("PLUGIN.DISABLED");
    }

}
