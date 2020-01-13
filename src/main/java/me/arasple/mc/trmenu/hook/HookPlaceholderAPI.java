package me.arasple.mc.trmenu.hook;

import io.izzel.taboolib.module.inject.THook;
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

        if (type.length != 2) {
            return null;
        }

        String[] args = type[1].split("\\|");
        if ("version".equalsIgnoreCase(type[0])) {
            return String.valueOf(TrMenu.getVersion());
        } else if ("menus".equalsIgnoreCase(args[1])) {
            return String.valueOf(TrMenuAPI.getMenus().size());
        } else if ("tapapi".equalsIgnoreCase(type[0])) {
            if (args.length == 2) {
                Player player = Bukkit.getPlayerExact(args[0]);
                player = player == null ? p : player;
                return PlaceholderAPI.setBracketPlaceholders(player, args[1]);
            }
        } else if ("query".equalsIgnoreCase(type[0])) {
            if (args.length >= 1) {
                return Querier.query(PlaceholderAPI.setBracketPlaceholders(p, args[0]), args.length >= 2 ? PlaceholderAPI.setBracketPlaceholders(p, args[1]) : null);
            }
        }
        return null;
    }

}
