package me.arasple.mc.trmenu.module.internal.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.util.bukkit.Heads
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author Arasple
 * @date 2021/1/27 12:14
 */
@TListener
class ListenerJoin : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        Tasks.task(true) {
            // 缓存玩家头颅备用
            Heads.getPlayerHead(player.name)
            // 加载 Metadata - Data 数据
            Metadata.loadData(player)
        }
    }

}