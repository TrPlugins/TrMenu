package me.arasple.mc.trmenu.modules.listener.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.modules.function.Shortcuts
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

/**
 * @author Arasple
 * @date 2020/7/28 15:35
 */
@TListener(version = ">=10900")
class ListenerSwapHand : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun offHand(e: PlayerSwapHandItemsEvent) {
        if (Shortcuts.offhand(e.player)) {
            e.isCancelled = true
        }
    }

}