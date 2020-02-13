package me.arasple.mc.trmenu.hook;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.inject.THook;
import io.izzel.taboolib.module.locale.TLocaleLoader;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.utils.Querier;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/10/6 8:08
 * - %trmenu_tapapi_[玩家ID] | {PAPI变量}% ———— 若目标玩家在线，则返回目标玩家为请求玩家的PAPI变量
 * - %trmenu_query_[URL] | [QUERY] ---- 读取URL中JSON信息, 缓存读取中则返回 Loading... %
 * - %trmenu_query_[URL] | [QUERY] ---- 读取URL中JSON信息, 缓存读取中则返回 Loading... %
 * - %trmenu_greater_{input1}_{input2}_{containEquals}
 * - %trmenu_smaller_{input1}_{input2}_{containEquals}
 */
@THook
public class HookPlaceholderAPI extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "trmenu";
    }

    @Override
    public String getAuthor() {
        return "Arasple";
    }

    @Override
    public String getVersion() {
        return TrMenu.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null || !p.isOnline()) {
            return null;
        }

        String[] type = params.split("_", 2);
        String[] args = type.length >= 2 ? type[1].split("\\|") : new String[]{};
        String[] arguments = type.length >= 2 ? PlaceholderAPI.setBracketPlaceholders(p, type[1]).split("_") : new String[]{};

        if ("version".equalsIgnoreCase(type[0])) {
            return TrMenu.getPlugin().getDescription().getVersion();
        } else if ("menus".equalsIgnoreCase(type[0])) {
            return String.valueOf(TrMenuAPI.getMenus().size());
        } else if ("locale".equalsIgnoreCase(type[0])) {
            return TLocaleLoader.getLocalPriorityFirst(TrMenu.getPlugin());
        } else if (type.length == 2) {
            if (("tapapi".equalsIgnoreCase(type[0]))) {
                if (args.length == 2) {
                    Player player = Bukkit.getPlayerExact(args[0]);
                    player = player == null ? p : player;
                    return PlaceholderAPI.setBracketPlaceholders(player, args[1]);
                }
            } else if ("query".equalsIgnoreCase(type[0])) {
                if (args.length >= 1) {
                    return Querier.query(PlaceholderAPI.setBracketPlaceholders(p, args[0]), args.length >= 2 ? PlaceholderAPI.setBracketPlaceholders(p, args[1]) : null);
                }
            } else if ("greater".equalsIgnoreCase(type[0])) {
                if (arguments.length >= 2) {
                    boolean equals = arguments.length >= 3 && Boolean.parseBoolean(arguments[2]);
                    return String.valueOf(equals ? NumberUtils.toDouble(arguments[0]) >= NumberUtils.toDouble(arguments[1]) : NumberUtils.toDouble(arguments[0]) > NumberUtils.toDouble(arguments[1]));
                }
            } else if ("smaller".equalsIgnoreCase(type[0])) {
                if (arguments.length >= 2) {
                    boolean equals = arguments.length >= 3 && Boolean.parseBoolean(arguments[2]);
                    return String.valueOf(equals ? NumberUtils.toDouble(arguments[0]) <= NumberUtils.toDouble(arguments[1]) : NumberUtils.toDouble(arguments[0]) < NumberUtils.toDouble(arguments[1]));
                }
            }
        }
        return null;
    }

}
