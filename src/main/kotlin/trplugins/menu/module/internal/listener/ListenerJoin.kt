package trplugins.menu.module.internal.listener

import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.util.bukkit.Heads
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @date 2021/1/27 12:14
 */
object ListenerJoin {

    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        submit(async = true) {
            // 缓存玩家头颅备用
            Heads.getPlayerHead(player.name)
            // 加载 Metadata - Data 数据
            Metadata.loadData(player)
        }
    }

}