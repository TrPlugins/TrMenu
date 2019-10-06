package me.arasple.mc.trmenu.hook;

import io.izzel.taboolib.module.inject.THook;
import me.arasple.mc.trmenu.TrMenu;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/10/6 8:08
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
        return null;
    }

}
