package me.arasple.mc.trmenu.hook;

import io.izzel.taboolib.module.inject.THook;
import me.arasple.mc.trmenu.TrMenu;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/10/6 8:08
 * -
 * - %trmenu_tapapi_[玩家ID]_{PAPI变量}% ———— 若目标玩家在线，则返回目标玩家为请求玩家的PAPI变量
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

        String[] args = params.toLowerCase().split("_");

        if ("tapapi".equals(args[0])) {
            if (args.length == 3) {
                Player player = Bukkit.getPlayerExact(args[1]);
                player = player == null ? p : player;
                return PlaceholderAPI.setBracketPlaceholders(player, args[2]);
            }
        }
        return null;
    }

}
