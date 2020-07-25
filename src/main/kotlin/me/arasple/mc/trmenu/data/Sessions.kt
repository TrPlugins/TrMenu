package me.arasple.mc.trmenu.data

import me.arasple.mc.trmenu.api.factory.MenuFactorySession
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.display.menu.MenuLayout
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/24 20:58
 */
object Sessions {

    const val TRMENU_WINDOW_ID: Int = 119

    private val sessions = mutableMapOf<UUID, MenuSession>()
    private val factorySessions = mutableMapOf<UUID, MenuFactorySession>()

    fun Player.getMenuSession() = sessions.computeIfAbsent(this.uniqueId) { MenuSession() }

    fun Player.setMenuSession(menu: Menu?, layout: MenuLayout.Layout?, page: Int) = this.getMenuSession().set(menu, layout, page)

    fun Player.getMenuFactorySession() = factorySessions.computeIfAbsent(this.uniqueId) { MenuFactorySession(this, null, mutableMapOf(), mutableMapOf()) }

    fun Player.removeMenuSession() = sessions.remove(this.uniqueId)

    fun MenuSession.getPlayer() = sessions.entries.firstOrNull { it.value == this }?.key?.let { Bukkit.getPlayer(it) }

}