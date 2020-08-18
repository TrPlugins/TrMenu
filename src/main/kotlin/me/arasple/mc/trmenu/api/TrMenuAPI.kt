package me.arasple.mc.trmenu.api

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.api.factory.MenuFactory
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

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
    fun getMenus() = Menu.getMenus()

    /**
     * Get all loaded menus
     *
     * @return menus loaded by all plugins
     */
    @JvmStatic
    fun getAllMenus() = Menu.getAllMenus()

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
    fun getOpeningMenu(player: Player) = getSession(player).menu

    /**
     * Get the menu session of a player
     *
     * @param player Player
     * @return MenuSession
     */
    @JvmStatic
    fun getSession(player: Player) = player.getMenuSession()

    /**
     * Check if the player is viewing a menu
     *
     * @param player Player
     * @return is viewing a menu
     */
    @JvmStatic
    fun isViewingMenu(player: Player) = !getSession(player).isNull()

    /**
     * Register a new action that can be use
     *
     * @param action Action
     */
    @JvmStatic
    fun registerAction(action: Action) = Actions.registerAction(action)


    /**
     * Build a MenuFactory
     */
    fun buildMenu() = buildMenu(TrMenu.plugin)

    fun buildMenu(plugin: Plugin) = MenuFactory(plugin)

}