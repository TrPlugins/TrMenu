package me.arasple.mc.trmenu.modules.display

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.api.Extends.getMenuSesssionCheck
import me.arasple.mc.trmenu.api.nms.NMS
import me.arasple.mc.trmenu.modules.display.icon.IconProperty
import me.arasple.mc.trmenu.modules.display.icon.IconSettings
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
import me.arasple.mc.trmenu.util.Msger
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/**
 * @author Arasple
 * @date 2020/5/30 13:48
 */
class Icon(
    val id: String,
    val settings: IconSettings,
    val defIcon: IconProperty,
    val subIcons: List<IconProperty>,
    val currentIndex: MutableMap<UUID, Int>
) {

    fun setItemStack(player: Player, session: Menu.Session, updateDynamicSlots: Boolean) {
        Mirror.async("Icon:setItemStack(async)") {
            setItemStackSync(player, session, updateDynamicSlots)
        }
    }

    fun setItemStackSync(player: Player, session: Menu.Session, updateDynamicSlots: Boolean) {
        val property = getIconProperty(player)
        val slots = if (updateDynamicSlots) property.display.getPosition(
            player,
            session.page
        ) else property.display.getCurrentPosition(player, session.page)
        setItemStack(player, session, property, slots)
    }

    fun setItemStack(player: Player, session: Menu.Session, property: IconProperty, slots: Set<Int>?) {
        val item = property.display.createDisplayItem(player)
        slots?.forEach { NMS.sendOutSlot(player, it, item) }
        if (property.display.isAnimatedPosition(session.page)) NMS.sendClearNonIconSlots(player, session)
    }

    fun displayItemStack(player: Player) {
        player.getMenuSesssionCheck()?.let {
            setItemStack(player, it, true)
        }
    }

    fun startUpdateTasks(player: Player, menu: Menu) {
        Tasks.delay(5, true) {
            val session = player.getMenuSession()
            val sessionId = session.id

            settings.collectUpdatePeriods().let { it ->
                if (it.isEmpty()) return@let
                it.forEach {
                    val period = it.key.toLong()
                    menu.tasking.task(player, object : BukkitRunnable() {
                        override fun run() {
                            Mirror.eval("Icon:updateItem(sync)") {
                                if (session.isDifferent(sessionId)) cancel()
                                else {
                                    getIconProperty(player).display.nextFrame(player, it.value, session)
                                    setItemStack(player, session, true)
                                }
                            }
                        }
                    }.runTaskTimer(TrMenu.plugin, period, period))
                }
            }

            if (settings.refresh > 0 && subIcons.isNotEmpty()) {
                val period = settings.refresh.toLong()
                menu.tasking.task(player, object : BukkitRunnable() {
                    override fun run() {
                        Mirror.eval("Icon:refreshIcon(sync)") {
                            if (session.isDifferent(sessionId)) cancel()
                            else if (refreshIcon(player)) {
                                displayItemStack(player)
                            }
                        }
                    }
                }.runTaskTimer(TrMenu.plugin, period, period))
            }
        }
    }

    fun getIconProperty(player: Player) = getIconPropertyIndex(player).let { if (it >= 0) subIcons[it] else defIcon }

    fun getIconPropertyIndex(player: Player) = currentIndex.computeIfAbsent(player.uniqueId) { -1 }

    fun refreshIcon(player: Player): Boolean {
        subIcons.indexOfFirst { it.evalCondition(player) }.let {
            currentIndex[player.uniqueId] = it
            if (it >= 0) {
                Msger.debug(player, "ICON.SUB-ICON-REFRESHED", id, currentIndex[player.uniqueId].toString())
                return true
            }
            return false
        }
    }

    fun refreshCache(player: Player) {
        defIcon.display.refresh(player)
        subIcons.forEach { sub ->
            sub.display.refresh(player)
        }
    }

    fun resetCache(uniqueId: UUID) {
        defIcon.display.item.cache.remove(uniqueId)
        subIcons.forEach { sub ->
            sub.display.item.cache.remove(uniqueId)
        }
    }

    fun isInPage(page: Int): Boolean {
        return defIcon.display.position.containsKey(page) || subIcons.any { it.display.position.containsKey(page) }
    }

}