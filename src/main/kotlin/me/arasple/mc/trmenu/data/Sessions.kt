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

    private val sessions = mutableMapOf<UUID, MenuSession>()
    private val factorySessions = mutableMapOf<UUID, MenuFactorySession>()

    fun getMenuSession(player: Player): MenuSession {
        return sessions.computeIfAbsent(player.uniqueId) { MenuSession() }
    }

    fun setMenuSession(player: Player, menu: Menu?, layout: MenuLayout.Layout?, page: Int) {
        getMenuSession(player).set(menu, layout, page)
    }

    fun getMenuFactorySession(player: Player): MenuFactorySession {
        return factorySessions.computeIfAbsent(player.uniqueId) { MenuFactorySession(player, null, mutableMapOf(), mutableMapOf()) }
    }

    fun removeMenuSession(player: Player) {
        sessions.remove(player.uniqueId)
    }

    fun getPlayer(session: MenuSession): Player? {
        return sessions.entries.firstOrNull { it.value == session }?.key?.let { Bukkit.getPlayer(it) }
    }

}