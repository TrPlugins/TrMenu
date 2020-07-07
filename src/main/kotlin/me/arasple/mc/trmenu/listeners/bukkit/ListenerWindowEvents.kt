package me.arasple.mc.trmenu.listeners.bukkit

import io.izzel.taboolib.module.packet.Packet
import io.izzel.taboolib.module.packet.TPacket
import me.arasple.mc.trmenu.api.events.MenuClickEvent
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.data.MenuSession.Companion.TRMENU_WINDOW_ID
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/6 22:25
 */
object ListenerWindowEvents {

    @TPacket(type = TPacket.Type.RECEIVE)
    fun playInWindowClick(player: Player, packet: Packet): Boolean {
        try {
            if (packet.`is`("PacketPlayInWindowClick")) {
                if (player.hasMetadata("TrMenu:Debug")) {
                    player.sendMessage(arrayOf("§3Packet§fInfo §7WindowClick: ", "", "§7Window ID: §a${packet.read("a")}", "§7Window Slot/Button: §a${packet.read("slot")} / ${packet.read("button")}", "§7Window D: §a${packet.read("d")}", "§7Window ClickType: §a${packet.read("shift")}"))
                }

                val session = MenuSession.session(player)
                val menu = session.menu
                val layout = session.layout
                val slot = packet.read("slot") as Int
                if (menu != null && layout != null) {
                    val size = layout.size()
                    val icon = menu.getIcon(player, session.page, slot)
                    val type = PacketsHandler.getClickType(packet.read("shift"), packet.read("button") as Int, slot)

                    PacketsHandler.sendCancelSlot(player)
                    icon?.displayItemStack(player)

                    if (slot >= size || type.isItemMoveable()) {
                        if (menu.settings.options.hidePlayerInventory)
                            PacketsHandler.clearInventory(player, size, TRMENU_WINDOW_ID)
                        else
                            PacketsHandler.resetInventory(player, size, TRMENU_WINDOW_ID)

                    }

                    MenuClickEvent(player, slot, menu, icon, type).async(true).call()
                    return false
                }
            } else if (packet.`is`("PacketPlayInCloseWindow")) {
                val session = MenuSession.session(player)
                if (!session.isNull()) {
//					if (session.isEditing && !EditorAPI.isToggling(player)) {
//						Tasks.runDelayTask(Runnable {
//							EditorAPI.toggling(player)
//							EditorAPI.listBuilders(player)
//						}, 1)
//					}
                    session.layout?.close(player, false)
                    player.updateInventory()

                }
            }
        } catch (e: Throwable) {
            Msger.printErrors("PACKET", e, packet.javaClass.simpleName)
        }
        return true
    }

}