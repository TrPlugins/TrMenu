package me.arasple.mc.trmenu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import me.arasple.mc.trmenu.menu.Menu;

import java.io.File;
import java.util.List;

/**
 * @author Arasple
 * @document https://trmenu.trixey.cn
 * @since 2019.10.2
 */
@Plugin.Version(5.15)
public final class TrMenu extends Plugin {

    @TInject
    private static TrMenu instance;
    private static List<Menu> menus = Lists.newArrayList();
    @TInject(value = "settings.yml", locale = "LOCALE-PRIORITY")
    private static TConfig settings;
    @TInject(state = TInject.State.LOADING, init = "init", active = "load", cancel = "unload")
    private static TrMenuLoader loader;

    public static List<Menu> getMenus() {
        return menus;
    }

    public static TConfig getSettings() {
        return settings;
    }

    public static double getTrVersion() {
        return 1.16;
    }

    public static File getPluginFile() {
        return instance.getFile();
    }

}
