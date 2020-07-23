package me.arasple.mc.trmenu.data

import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/6 21:56
 */
class MenuSession(var menu: Menu?, var layout: MenuLayout.Layout?, var page: Int, var fromLayout: MenuLayout.Layout?) {

    fun isNull() = menu == null || layout == null

    fun set(menu: Menu?, layout: MenuLayout.Layout?, page: Int) {
        Msger.debug("SESSION", menu?.id ?: "null", page)

        this.menu = menu
        this.layout = layout
        this.page = page
    }

    fun isDifferent(menu: Menu, currentPage: Int) = this.menu != menu || page != currentPage


    constructor() : this(null, null, 0, null)

    companion object {

        const val TRMENU_WINDOW_ID: Int = 119

        private val sessions = mutableMapOf<UUID, MenuSession>()

        fun session(player: Player): MenuSession = sessions.computeIfAbsent(player.uniqueId) { MenuSession() }

        fun remove(player: Player) = sessions.remove(player.uniqueId)

    }

}