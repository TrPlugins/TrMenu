package me.arasple.mc.trmenu.data

import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.display.menu.MenuLayout
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/6 21:56
 */
class MenuSession(var menu: Menu?, var layout: MenuLayout.Layout?, var page: Int) {

    fun isNull() = menu == null

    fun set(menu: Menu?, layout: MenuLayout.Layout?, page: Int){
        this.menu = menu
        this.layout = layout
        this.page = page
    }

    constructor() : this(null, null, 0)

    companion object {

        const val TRMENU_WINDOW_ID: Int = 119

        private val sessions = mutableMapOf<UUID, MenuSession>()

        fun session(player: Player): MenuSession = sessions.computeIfAbsent(player.uniqueId) { MenuSession() }

    }

}