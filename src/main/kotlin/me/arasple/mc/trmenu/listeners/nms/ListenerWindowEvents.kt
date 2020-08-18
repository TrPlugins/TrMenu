package me.arasple.mc.trmenu.listeners.nms

import io.izzel.taboolib.module.packet.Packet
import io.izzel.taboolib.module.packet.TPacket
import me.arasple.mc.trmenu.api.Extends.getMenuFactorySession
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.api.events.MenuClickEvent
import me.arasple.mc.trmenu.api.events.MenuCloseEvent
import me.arasple.mc.trmenu.api.factory.MenuFactorySession
import me.arasple.mc.trmenu.api.factory.task.ClickTask
import me.arasple.mc.trmenu.api.factory.task.ClickTask.Action.ACCESS
import me.arasple.mc.trmenu.api.factory.task.ClickTask.Action.CANCEL_MODIFY
import me.arasple.mc.trmenu.api.factory.task.CloseTask
import me.arasple.mc.trmenu.api.inventory.InvClickType
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/7/6 22:25
 */
object ListenerWindowEvents {

    @TPacket(type = TPacket.Type.RECEIVE)
    fun playInWindowClick(player: Player, packet: Packet): Boolean {
        try {
            if (packet.`is`("PacketPlayInWindowClick")) {
                val slot = packet.read("slot") as Int
                val type = PacketsHandler.getClickType(packet.read("shift"), packet.read("button") as Int, slot)

                val factorySession = player.getMenuFactorySession()
                val session = player.getMenuSession()

                if (!factorySession.isNull() || !session.isNull()) {
                    val size =
                        if (!factorySession.isNull())
                            processMenuFactory(player, factorySession, slot, PacketsHandler.INSTANCE.asBukkitItem(packet.read("item")), type)
                        else
                            processMenuSession(player, session, slot, type)

                    /**
                     * 1.16+ 使用 OFFHAND 键移动菜单物品, 受原版限制, 暂时无解决方案
                     */

                    if (slot >= size || type.isItemMoveable()) {
                        val menu = session.menu
                        val factory = factorySession.menuFactory

                        if ((menu != null && menu.settings.options.hidePlayerInventory) || factory != null) {
                            PacketsHandler.clearInventory(player, size)
                        } else {
                            PacketsHandler.resetInventory(player, size)
                        }
                    }

                    return false
                }
            } else if (packet.`is`("PacketPlayInCloseWindow")) {
                val factorySession = player.getMenuFactorySession()
                val session = player.getMenuSession()

                if (!factorySession.isNull()) {
                    factorySession.menuFactory!!.closeTask?.run(CloseTask.Event(player, factorySession, factorySession.menuFactory!!))
                    factorySession.reset()
                    player.updateInventory()
                } else if (!session.isNull()) {
                    session.menu?.close(player, session.page, MenuCloseEvent.Reason.PLAYER, false, silent = false)
                }
            }
        } catch (e: Throwable) {
            Msger.printErrors("PACKET", e, packet.javaClass.simpleName)
        }
        return true
    }

    private fun processMenuSession(player: Player, session: MenuSession, slot: Int, type: InvClickType): Int {
        val menu = session.menu!!
        val size = session.layout!!.size()

        menu.getIcon(player, session.page, slot).let {
            PacketsHandler.sendCancelSlot(player)
            it?.displayItemStack(player)
            MenuClickEvent(player, slot, menu, it, type).async(true).call()
        }

        return size
    }

    private fun processMenuFactory(player: Player, session: MenuFactorySession, slot: Int, itemStack: ItemStack?, type: InvClickType): Int {
        val factory = session.menuFactory!!

        (session.getItem(slot) ?: return -1).let {
            factory.clickTask?.run(ClickTask.Event(player, session, factory, slot, it.first, it.second, itemStack, type)).let { action ->
                val item = it.second
                if (item != null && action != ACCESS) {
                    PacketsHandler.sendCancelSlot(player)
                    if (action == CANCEL_MODIFY) session.setItem(slot, item, true)
                }
            }
        }

        return factory.size()
    }

}