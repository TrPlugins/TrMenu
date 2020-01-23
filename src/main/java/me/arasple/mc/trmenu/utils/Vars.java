package me.arasple.mc.trmenu.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.data.ArgsCache;
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
        return PlaceholderAPI.setPlaceholders(player, string);
    }

}
