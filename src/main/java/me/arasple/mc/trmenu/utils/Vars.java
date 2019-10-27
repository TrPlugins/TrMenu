package me.arasple.mc.trmenu.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/6 21:59
 */
@TFunction(enable = "init")
public class Vars {

    private static boolean enabled;

    static void init() {
        enabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled();
        if (isEnabled()) {
            TLocale.sendToConsole("PLUGIN.HOOKED", "PlaceholderAPI");
        }
    }

    public static String replace(Player player, String string) {
        return setPlaceholders(player, Strings.replaceWithOrder(string, ArgsCache.getPlayerArgs(player)));
    }

    public static List<String> replace(Player player, List<String> strings) {
        List<String> results = Lists.newArrayList();
        strings.forEach(str -> results.add(replace(player, str)));
        return results;
    }

    private static String setPlaceholders(Player player, String string) {
        return isEnabled() ? PlaceholderAPI.setPlaceholders(player, string) : TLocale.Translate.setColored(string);
    }

    public static boolean isEnabled() {
        return enabled;
    }


}
