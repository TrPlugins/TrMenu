package me.arasple.mc.trmenu.api

import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/7 10:58
 */
object TrMenuAPI {

    /**
     * Get loaded menus of this plugin
     *
     * @return menus loaded by TrMenu
     */
    @JvmStatic
    fun getMenus(): List<Menu> = Menu.getMenus()

    /**
     * Get the menu loaded
     *
     * @param menuId Menu Id
     * @return Menu
     */
    @JvmStatic
    fun getMenuById(menuId: String?): Menu? = getMenus().stream().filter { menu: Menu -> menu.id.equals(menuId, ignoreCase = true) }.findFirst().orElse(null)

    /**
     * Get the menu player is currently viewing
     *
     * @param player Player
     * @return Menu
     */
    @JvmStatic
    fun getOpeningMenu(player: Player): Menu? = getMenus().stream().filter { menu: Menu -> menu.viewers.contains(player) }.findFirst().orElse(null)

    /**
     * Get the menu session of a player
     *
     * @param player Player
     * @return MenuSession
     */
    @JvmStatic
    fun getSession(player: Player): MenuSession = MenuSession.session(player)

    /**
     * Check if the player is viewing a menu
     *
     * @param player Player
     * @return is viewing a menu
     */
    @JvmStatic
    fun isViewingMenu(player: Player): Boolean = getMenus().stream().anyMatch { menu: Menu -> menu.viewers.contains(player) }

    /**
     * Register a new action that can be use
     *
     * @param action Action
     */
    @JvmStatic
    fun registerAction(action: Action) = Actions.registerAction(action)


}