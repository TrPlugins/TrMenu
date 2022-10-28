package cc.trixey.mc.dinvero.nms

import cc.trixey.mc.dinvero.InveroManager
import cc.trixey.mc.dinvero.event.InveroCloseEvent
import cc.trixey.mc.dinvero.event.InveroInteractEvent
import cc.trixey.mc.dinvero.window.InteractType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.PacketReceiveEvent
import taboolib.platform.util.sendActionBar

/**
 * @author Arasple
 * @since 2022/10/20
 */
object WindowListener {

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        val player = e.player
        val packet = e.packet

        when (packet.name) {
            "PacketPlayInWindowClick" -> {
                val id = packet.read<Int>(if (isUniversal) "containerId" else "a") ?: return
                val mode = packet.read<Any>(if (isUniversal) "clickType" else "shift").toString().let { name ->
                    InteractType.Mode.values().find { it.name.equals(name, false) }
                } ?: return
                val button = packet.read<Int>(if (isUniversal) "buttonNum" else "button") ?: return
                val slot = packet.read<Int>(if (isUniversal) "slotNum" else "slot") ?: return
                val interactType: InteractType = InteractType.find(mode, button, slot) ?: return
                val clickedItem = packet.read<Any?>(if (isUniversal) "carriedItem" else "item")?.asCraftMirror()

                InveroManager.findViewingInvero(player, id)?.apply {
                    amend()
                    if (interactType.isIgnoreSuggested) {
                        updateQuickCraft(player, interactType, clickedItem)
                    } else {
                        InveroInteractEvent(player, this, interactType, slot, clickedItem).apply {
                            call()
                            interactCallback.forEach { it(this) }
                            if (isCancelled) {
                                player.sendCancelCoursor()
                                refreshItem(slot)
                            }
                        }
                    }
                    e.isCancelled = true
                }

                player.sendActionBar("ContainerID: $id")
            }

            "PacketPlayInCloseWindow" -> {
                val id = packet.read<Int>(if (isUniversal) "containerId" else "id") ?: return

                InveroManager.findViewingInvero(player, id)?.apply {
                    view.close(player, true)

                    InveroCloseEvent(player, this).apply {
                        call()
                        closeCallback(this)
                        if (shouldUpdate) {
                            player.updateInventory()
                        }
                    }
                    e.isCancelled = true
                }
            }
        }
    }
}