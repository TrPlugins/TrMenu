package me.arasple.mc.trmenu.api

import me.arasple.mc.trmenu.module.display.Menu


/**
 * @author Arasple
 * @date 2021/1/25 9:54
 */
object TrMenuAPI {

    /**
     * Get the menu through its ID
     *
     * @return Menu
     * @see Menu
     */
    fun getMenuById(id: String): Menu? {
        return Menu.menus.find { it.id == id }
    }

}