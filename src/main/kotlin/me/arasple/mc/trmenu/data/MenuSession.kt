package me.arasple.mc.trmenu.data

import me.arasple.mc.trmenu.data.Sessions.getPlayer
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.utils.Msger

/**
 * @author Arasple
 * @date 2020/7/6 21:56
 */
class MenuSession(var menu: Menu?, var layout: MenuLayout.Layout?, var page: Int, var fromLayout: MenuLayout.Layout?) {

    fun isNull() = menu == null || layout == null

    fun set(menu: Menu?, layout: MenuLayout.Layout?, page: Int) {
        Msger.debug("SESSION", this.getPlayer()?.name ?: "null", menu?.id ?: "null", page)

        this.menu = menu
        this.layout = layout
        this.page = page
    }

    fun isDifferent(menu: Menu, currentPage: Int) = this.menu != menu || page != currentPage


    constructor() : this(null, null, 0, null)

}