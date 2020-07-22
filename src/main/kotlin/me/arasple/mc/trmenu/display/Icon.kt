package me.arasple.mc.trmenu.display

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.data.MenuSession.Companion.TRMENU_WINDOW_ID
import me.arasple.mc.trmenu.display.icon.IconClickHandler
import me.arasple.mc.trmenu.display.icon.IconDisplay
import me.arasple.mc.trmenu.display.icon.IconSettings
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/**
 * @author Arasple
 * @date 2020/5/30 13:48
 */
class Icon(val id: String, val settings: IconSettings, val defIcon: IconProperty, val subIcons: List<IconProperty>, val currentIndex: MutableMap<UUID, Int>) {

    constructor(id: String, settings: IconSettings, defIcon: IconProperty, subIcons: List<IconProperty>) : this(id, settings, defIcon, subIcons, mutableMapOf())

    fun displayIcon(player: Player, menu: Menu) {
        refreshIcon(player)
        displayItemStack(player)
        startUpdateTasks(player, menu)
    }

    fun displayItemStack(player: Player) {
        val session = MenuSession.session(player)

        if (!session.isNull()) {
            val property = getIconProperty(player)
            val slots = property.display.getPosition(player, session.page)
            val item = property.display.createDisplayItem(player)
            slots?.forEach { PacketsHandler.sendOutSlot(player, TRMENU_WINDOW_ID, it, item) }
            if (property.display.isAnimatedPosition(session.page)) PacketsHandler.sendClearNonIconSlots(player, session)
        }
    }

    private fun startUpdateTasks(player: Player, menu: Menu) {
        val session = MenuSession.session(player)

        // 图标物品更新
        settings.collectUpdatePeriods().let { it ->
            if (it.isEmpty()) return@let
            it.forEach {
                object : BukkitRunnable() {
                    override fun run() {
                        if (session.menu != menu) cancel()
                        else {
                            Msger.debug(player, "ICON.DISPLAY-UPDATE", false, id, it.key, it.value.joinToString(",", "{", "}"))
                            getIconProperty(player).display.nextFrame(player, it.value, session.page)
                            displayItemStack(player)
                        }
                    }
                }.runTaskTimerAsynchronously(TrMenu.plugin, it.key.toLong(), it.key.toLong())
            }
        }

        // 子图标刷新
        if (settings.refresh > 0 && subIcons.isNotEmpty()) {
            object : BukkitRunnable() {
                override fun run() {
                    if (session.menu != menu) cancel()
                    else if (refreshIcon(player)) {
                        displayItemStack(player)
                    }
                }
            }.runTaskTimerAsynchronously(TrMenu.plugin, settings.refresh.toLong(), settings.refresh.toLong())
        }
    }

    fun getIconProperty(player: Player) = getIconPropertyIndex(player).let { if (it >= 0) subIcons[it] else defIcon }

    fun getIconPropertyIndex(player: Player) = currentIndex.computeIfAbsent(player.uniqueId) { -1 }

    fun refreshIcon(player: Player): Boolean {
        subIcons.sortedBy { it.priority }.forEachIndexed { index, subIcon ->
            if (Scripts.expression(player, subIcon.condition).asBoolean()) {
                currentIndex[player.uniqueId] = index
                Msger.debug(player, "ICON.SUB-ICON-REFRESHED", currentIndex[player.uniqueId].toString())
                return true
            }
        }
        currentIndex[player.uniqueId] = -1
        return false
    }

    class IconProperty(var priority: Int, var condition: String, val display: IconDisplay, val clickHandler: IconClickHandler)

}