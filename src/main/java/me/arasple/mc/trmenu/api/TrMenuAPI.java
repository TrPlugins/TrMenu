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

    /**
     * 通过菜单ID取得菜单
     *
     * @param menuId id
     * @return TrMenu菜单
     */
    public static Menur getMenu(String menuId) {
        return TrMenu.getMenus().stream().filter(menu -> menu.getName().equals(menuId)).findFirst().orElse(null);
    }

    /**
     * 为一名玩家打开一个菜单
     *
     * @param player 目标玩家
     * @param id     菜单ID
     * @return 是否存在菜单
     */
    public static boolean openMenu(Player player, String id) {
        Menur menu = getMenu(id);
        if (menu != null) {
            menu.open(player);
            return true;
        }
        return false;
    }

    /**
     * 取得所有菜单的ID集合
     *
     * @return 集合
     */
    public static List<String> getMenuIds() {
        List<String> menus = Lists.newArrayList();
        getMenus().forEach(m -> menus.add(m.getName()));

        return menus;
    }

    /**
     * 通过打开菜单的命令取得菜单
     *
     * @param cmd 命令
     * @return 菜单
     */
    public static Menur getMenuByCommand(String cmd) {
        return getMenus().stream().filter(menu -> menu.getOpenCommands().contains(cmd)).findFirst().orElse(null);
    }

    /**
     * 取得 TrMenu 已加载的所有菜单
     *
     * @return 菜单集合
     */
    public static List<Menur> getMenus() {
        return TrMenu.getMenus();
    }

}
