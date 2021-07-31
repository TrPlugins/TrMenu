package me.arasple.mc.trmenu.module.internal.listener

import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.util.Tasks
import me.arasple.mc.trmenu.util.bukkit.Heads
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.SubscribeEvent

/**
 * @author Arasple
 * @date 2021/1/27 12:14
 */
object ListenerJoin {

    @SubscribeEvent
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