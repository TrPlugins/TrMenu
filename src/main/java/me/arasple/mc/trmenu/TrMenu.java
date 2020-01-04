package me.arasple.mc.trmenu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import me.arasple.mc.trmenu.menu.Menu;

import java.util.List;

/**
 * @author Arasple
 * @document https://arasple.gitbook.io/trmenu
 * @since 2019.10.2
 */
@TrMenuPlugin.Version(5.13)
public final class TrMenu extends TrMenuPlugin {

    @TInject("settings.yml")
    private static TConfig settings;
    @TInject(state = TInject.State.LOADING, init = "init", active = "load", cancel = "unload")
    private static TrMenuLoader loader;

    private static List<Menu> menus = Lists.newArrayList();

    public static List<Menu> getMenus() {
        return menus;
    }

    public static TConfig getSettings() {
        return settings;
    }

    public static double getTrVersion() {
        return 1.11;
    }

}
