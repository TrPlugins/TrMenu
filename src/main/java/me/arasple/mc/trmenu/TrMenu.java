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

    @TInject(value = "settings.yml", locale = "LOCALE-PRIORITY")
    public static final TConfig SETTINGS = null;
    @TInject
    private static final TrMenu INSTANCE = null;
    @TInject(state = TInject.State.LOADING, init = "init", active = "active", cancel = "cancel")
    private static final TrMenuLoader LOADER = null;

    private static List<Menu> menus = Lists.newArrayList();

    public static List<Menu> getMenus() {
        return menus;
    }

    public static double getTrVersion() {
        return 1.19;
    }

    public static File getPluginFile() {
        return INSTANCE.getFile();
    }

}
