package me.arasple.mc.trmenu.display.icon

import me.arasple.mc.trmenu.api.inventory.MenuClickType
import me.arasple.mc.trmenu.display.function.Reaction
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 14:06
 */
class IconClickHandler(val handlers: List<Handler>) {

    fun onClick(player: Player, clickType: MenuClickType) {
        handlers.filter { it.isTriggered(clickType) }.forEach {
            it.react(player)
        }
    }

    class Handler(val triggers: Set<MenuClickType>, val reactions: List<Reaction>) {

        fun isTriggered(clickType: MenuClickType) = triggers.any { it == clickType }

        fun react(player: Player) = reactions.sortedBy { it.priority }.forEach { if (!it.react(player)) return }

    }


}