package me.arasple.mc.trmenu.modules.display.icon

import me.arasple.mc.trmenu.api.inventory.InvClickType
import me.arasple.mc.trmenu.modules.display.function.Reactions
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 14:06
 */
class IconClickHandler(val handlers: List<Handler>) {

    fun onClick(player: Player, clickType: InvClickType) {
        handlers.filter { it.isTriggered(clickType) }.forEach { it.react(player) }
    }

    data class Handler(val triggers: Set<InvClickType>, val reactions: Reactions) {

        fun isTriggered(clickType: InvClickType) = triggers.any { it == clickType || it == InvClickType.ALL }

        fun react(player: Player) = reactions.eval(player)

    }


}