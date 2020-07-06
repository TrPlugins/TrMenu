package me.arasple.mc.trmenu.display

import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.display.menu.MenuSettings

/**
 * @author Arasple
 * @date 2020/5/30 13:24
 */
class Menu(val id: String, val configuration: MenuConfiguration, val settings: MenuSettings) {

    fun save() {
        configuration.getTitle()
    }

}