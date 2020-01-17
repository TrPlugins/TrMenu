package me.arasple.mc.trmenu.api;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 18:20
 */
public class TrMenuAPI {

    /**
     * Get menu by ID
     *
     * @param menuId id
     * @return TrMenu菜单
     */
    public static Menu getMenu(String menuId) {
        return TrMenu.getMenus().stream().filter(menu -> menu.getName().equals(menuId)).findFirst().orElse(null);
    }

    public static Menu getMenu(File loadedFrom) {
        return TrMenu.getMenus().stream().filter(menu -> menu.getLoadedPath().equals(loadedFrom.getAbsolutePath())).findFirst().orElse(null);
    }

    /**
     * Open a menu for player
     *
     * @param player player
     * @param id     menu id
     * @return whether success
     */
    public static boolean openMenu(Player player, String id) {
        Menu menu = getMenu(id);
        if (menu != null) {
            menu.open(player, true);
            return true;
        }
        return false;
    }

    /**
     * Get all the ids of menus
     *
     * @return list
     */
    public static List<String> getMenuIds() {
        List<String> menus = Lists.newArrayList();
        getMenus().forEach(m -> menus.add(m.getName()));

        return menus;
    }

    /**
     * Get menu by open command
     *
     * @param cmd command
     * @return menu
     */
    public static Menu getMenuByCommand(String cmd) {
        return getMenus().stream().filter(menu -> menu.getOpenCommands() != null && menu.getOpenCommands().contains(cmd)).findFirst().orElse(null);
    }

    /**
     * Get all loaded menus
     *
     * @return menus
     */
    public static List<Menu> getMenus() {
        return TrMenu.getMenus();
    }

}
