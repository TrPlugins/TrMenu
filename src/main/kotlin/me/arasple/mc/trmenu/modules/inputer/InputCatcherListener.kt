package me.arasple.mc.trmenu.modules.inputer

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.packet.Packet
import io.izzel.taboolib.module.packet.TPacket
import me.arasple.mc.trmenu.modules.inputer.InputCatcher.getCatcher
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * @author Arasple
 * @date 2020/7/21 10:12
 */
@TListener
class InputCatcherListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player
        val catcher = player.getCatcher()?.catchers?.currentElement(player) ?: return

        if (catcher.type == Catchers.Type.CHAT) {
            e.isCancelled = true
            catcher.input(player, e.message)
        }
    }

}