package me.arasple.mc.trmenu.module.internal.listener

import me.arasple.mc.trmenu.api.event.ReceptacleInteractEvent
import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowClose
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowSetSlot
import me.arasple.mc.trmenu.api.receptacle.window.vanilla.ClickType
import me.arasple.mc.trmenu.util.Tasks
import taboolib.common.platform.SubscribeEvent
import taboolib.module.nms.PacketReceiveEvent

object ListenerPackets {
    
    @SubscribeEvent
    private fun e(e: PacketReceiveEvent) {
        val receptacle = ReceptacleAPI.MANAGER.getViewingReceptacle(e.player) ?: return

        if (e.packet.name == ("PacketPlayInWindowClick") && e.packet.read<Int>("a") ?: -1 == 119) {
            val slot = e.packet.read<Int>("slot")
            val mode = e.packet.read<String>("shift")
            val button = e.packet.read<Int>("button")
            val clickType = ClickType.from(mode!!, button!!, slot!!) ?: return
            val evt = ReceptacleInteractEvent(e.player, receptacle, clickType, slot).also { it.call() }
            receptacle.callEventClick(e.player, evt)

            if (evt.isCancelled) PacketWindowSetSlot(-1, -1).send(e.player)

            return
        } else if (e.packet.name == ("PacketPlayInCloseWindow") && e.packet.read<Int>("id") ?: -1 == 119) {
            receptacle.close(e.player, false)

            // 防止关闭菜单后, 动态标题频率过快出现的卡假容器
            Tasks.delay(1L, true) {
                ReceptacleAPI.MANAGER.getViewingReceptacle(e.player) ?: kotlin.run {
                    e.player.updateInventory()
                }
            }
            Tasks.delay(4L, true) {
                ReceptacleAPI.MANAGER.getViewingReceptacle(e.player) ?: kotlin.run {
                    PacketWindowClose().send(e.player)
                }
            }

            return
        }

        return
    }
    
}