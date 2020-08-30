package me.arasple.mc.trmenu.modules.listener.bukkit

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.modules.function.Shortcuts
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent

/**
 * @author Arasple
 * @date 2020/8/20 17:21
 */
@TListener
class ListenerToggleSneak : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun toggleSneaking(e: PlayerToggleSneakEvent) {
        Tasks.task(true) {
            Shortcuts.setSneaking(e.player)
        }
    }

}