package me.arasple.mc.trmenu.display

import me.arasple.mc.trmenu.display.icon.IconClickHandler
import me.arasple.mc.trmenu.display.icon.IconDisplay
import me.arasple.mc.trmenu.display.icon.IconSettings
import me.arasple.mc.trmenu.modules.script.Scripts
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/5/30 13:48
 */
class Icon(val id: String, val settings: IconSettings, val defIcon: IconProperty, val subIcons: List<IconProperty>, val currentIndex: MutableMap<UUID, Int>) {

    fun getIconProperty(player: Player) = getIconPropertyIndex(player).let { if (it >= 0) subIcons[it] else defIcon }

    fun getIconPropertyIndex(player: Player) = currentIndex.computeIfAbsent(player.uniqueId) { -1 }

    fun refreshIcon(player: Player) {
        subIcons.sortedBy { it.priority }.forEachIndexed { index, subIcon ->
            if (Scripts.expression(player, subIcon.condition).asBoolean()) {
                currentIndex[player.uniqueId] = index
                return
            }
        }
        currentIndex[player.uniqueId] = -1
    }

    class IconProperty(val priority: Int, val condition: String, val display: IconDisplay, val clickHandler: IconClickHandler)

}