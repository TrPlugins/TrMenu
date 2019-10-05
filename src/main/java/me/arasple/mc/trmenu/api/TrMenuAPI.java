package me.arasple.mc.trmenu.api;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.inv.Menur;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 18:20
 */
public class TrMenuAPI {

    public static Menur getMenu(String menuId) {
        return TrMenu.getMenus().stream().filter(menu -> menu.getName().equals(menuId)).findFirst().orElse(null);
    }

    public static void openMenu(Player player, String id) {
        Menur menu = getMenu(id);
        if (menu != null) {
            menu.open(player);
        }
    }

    public static List<Menur> getMenus() {
        return TrMenu.getMenus();
    }

    public static List<String> getMenuIds() {
        List<String> menus = Lists.newArrayList();
        getMenus().forEach(m -> menus.add(m.getName()));

        return menus;
    }

    public static Menur getMenuByCommand(String cmd) {
        return getMenus().stream().filter(menu -> menu.getOpenCommands().contains(cmd)).findFirst().orElse(null);
    }

}
