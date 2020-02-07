package me.arasple.mc.trmenu.api;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.menu.MenuHolder;
import me.arasple.mc.trmenu.nms.TrMenuNms;
import me.arasple.mc.trmenu.utils.TrUtils;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 18:20
 */
public class TrMenuAPI {

    /**
     * Get the menu a player is viewing
     *
     * @param player the player
     * @return menu
     */
    public static Menu getMenu(Player player) {
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
            return ((MenuHolder) player.getOpenInventory().getTopInventory().getHolder()).getMenu();
        }
        return null;
    }

    public static boolean isViewingMenu(Player player) {
        return getMenu(player) != null;
    }

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

    public static boolean openByShortcut(Player player, String read, String... args) {
        String[] menu = read != null ? read.split("\\|") : null;
        if (menu != null) {
            Menu trMenu = TrMenuAPI.getMenu(menu[0]);
            String perm = menu.length > 1 ? menu[1] : null;
            if (!((perm != null && !player.hasPermission(perm)) || trMenu == null)) {
                trMenu.open(player, args);
                return true;
            }
        } else if (Strings.nonEmpty(read) && read.split(":", 2).length > 1) {
            TrUtils.getInst().runAction(player, read.split(";"));
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
        return getMenus().stream().filter(menu -> menu.getOpenCommands() != null && menu.getOpenCommands().stream().anyMatch(cmd::matches)).findFirst().orElse(null);
    }

    /**
     * Get all loaded menus
     *
     * @return menus
     */
    public static List<Menu> getMenus() {
        return TrMenu.getMenus();
    }

    /**
     * Re-set a inventory's title while open
     *
     * @param player    player
     * @param inventory inventory
     * @param title     title
     */
    public static void setInventoryTitle(Player player, Inventory inventory, String title) {
        TrMenuNms.setTitle(player, inventory, Vars.replace(player, title));
    }

}
