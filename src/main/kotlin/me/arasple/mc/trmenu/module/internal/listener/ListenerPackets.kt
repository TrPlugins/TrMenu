package me.arasple.mc.trmenu.module.internal.listener

import taboolib.common.platform.SubscribeEvent
import taboolib.common.platform.submit
import taboolib.module.nms.PacketReceiveEvent
import taboolib.module.ui.receptacle.*

object ListenerPackets {
    
    @SubscribeEvent
    private fun e(e: PacketReceiveEvent) {
        val receptacle = e.player.getViewingReceptacle() ?: return

        if (e.packet.name == ("PacketPlayInWindowClick") && e.packet.read<Int>("a") ?: -1 == 119) {
            val slot = e.packet.read<Int>("slot")
            val mode = e.packet.read<String>("shift")
            val button = e.packet.read<Int>("button")
            val clickType = ReceptacleClickType.from(mode!!, button!!, slot!!) ?: return
            val evt = ReceptacleInteractEvent(e.player, receptacle, clickType, slot).also { it.call() }
            receptacle.callEventClick(evt)

            if (evt.isCancelled) PacketWindowSetSlot(windowId = -1, slot = -1).send(e.player)

            return
        } else if (e.packet.name == ("PacketPlayInCloseWindow") && e.packet.read<Int>("id") ?: -1 == 119) {
            receptacle.close(false)

            // 防止关闭菜单后, 动态标题频率过快出现的卡假容器
            submit(delay = 1L, async = true) {
                e.player.getViewingReceptacle() ?: kotlin.run {
                    e.player.updateInventory()
                }
            }
            submit(delay = 4L, async = true) {
                e.player.getViewingReceptacle() ?: kotlin.run {
                    PacketWindowClose().send(e.player)
                }
            }

            return
        }

        return
    }
    
}