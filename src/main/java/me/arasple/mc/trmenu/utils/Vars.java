package me.arasple.mc.trmenu.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.menu.Menu;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/10/6 21:59
 */
public class Vars {

    public static String replace(Player player, String string) {
        return string != null ? setPlaceholders(player, Strings.replaceWithOrder(string, ArgsCache.getPlayerArgs(player))) : null;
    }

    public static List<String> replace(Player player, List<String> strings) {
        List<String> results = Lists.newArrayList();
        strings.forEach(str -> results.add(replace(player, str)));
        return results.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static String setPlaceholders(Player player, String string) {
        return PlaceholderAPI.setPlaceholders(player, replaceMenuVariables(player, string));
    }

    private static String replaceMenuVariables(Player player, String string) {
        Menu menu = TrMenuAPI.getMenu(player);
        if (menu == null) {
            return string.replace("$input", ArgsCache.getInput().getOrDefault(player.getUniqueId(), ""));
        } else {
            return string
                    .replace("$input", ArgsCache.getInput().getOrDefault(player.getUniqueId(), ""))
                    .replace("{shape}", String.valueOf(menu.getShape(player)))
                    .replace("{page}", String.valueOf(menu.getShape(player) + 1))
                    ;
        }
    }

}
