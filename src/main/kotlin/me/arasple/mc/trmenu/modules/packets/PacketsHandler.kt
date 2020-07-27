package me.arasple.mc.trmenu.modules.packets

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.module.lite.SimpleReflection
import io.izzel.taboolib.module.lite.SimpleVersionControl
import io.izzel.taboolib.module.packet.TPacketHandler
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.inventory.InvClickType
import me.arasple.mc.trmenu.api.inventory.InvClickType.*
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.data.MetaPlayer.getInventoryContents
import me.arasple.mc.trmenu.data.Sessions
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.*
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/5 19:30
 */
abstract class PacketsHandler {

    abstract fun sendOpenWindow(player: Player, windowId: Int, inventoryType: InventoryType, size: Int, inventoryTitle: String)

    abstract fun sendCloseWindow(player: Player, windowId: Int)

    abstract fun sendOutSlot(player: Player, windowId: Int, slot: Int, itemStack: ItemStack)

    abstract fun sendRemoveSlot(player: Player, windowId: Int, slot: Int)

    abstract fun clearInventory(player: Player, startSlot: Int, windowId: Int)

    abstract fun asNMSItem(itemStack: ItemStack): Any

    abstract fun getClickTypeIndex(clickType: Any): Int

    abstract fun getPlayerTexture(player: Player): String

    companion object {

        lateinit var INSTANCE: PacketsHandler

        @TSchedule
        fun init() {
            val version = when {
                Version.isAfter(Version.v1_13) -> "16"
                Version.isAfter(Version.v1_9) -> "12"
                else -> "8"
            }
            INSTANCE = SimpleVersionControl.createNMS("me.arasple.mc.trmenu.modules.packets.impl.ImplPacketsHandler$version").useNMS().translate(TrMenu.plugin).getDeclaredConstructor().newInstance() as PacketsHandler
        }

        fun sendOpenWindow(player: Player, windowId: Int, inventoryType: InventoryType, size: Int, inventoryTitle: String) = INSTANCE.sendOpenWindow(player, windowId, inventoryType, size, inventoryTitle)

        fun sendCloseWindow(player: Player, windowId: Int) = INSTANCE.sendCloseWindow(player, windowId)

        fun sendOutSlot(player: Player, windowId: Int, slot: Int, itemStack: ItemStack) = INSTANCE.sendOutSlot(player, windowId, slot, itemStack)

        fun clearInventory(player: Player, size: Int, windowId: Int) = INSTANCE.clearInventory(player, size, windowId)

        fun getPlayerTexture(player: Player): String = INSTANCE.getPlayerTexture(player)

        fun sendCancelSlot(player: Player) = sendRemoveSlot(player, -1, -1)

        fun sendRemoveSlot(player: Player, windowId: Int, slot: Int) = INSTANCE.sendRemoveSlot(player, windowId, slot)

        fun resetInventory(player: Player, size: Int, windowId: Int) {
            val session = player.getMenuSession()
            val menu = session.menu
            if (menu != null) {
                val hasSlots = menu.getOccupiedSlots(player, session.page)
                val contents = player.getInventoryContents()
                for ((index, i) in (size..size + 35).withIndex()) {
                    if (hasSlots.contains(i)) {
                        continue
                    }
                    val item = contents[index]
                    if (item != null) sendOutSlot(player, windowId, i, item)
                    else sendRemoveSlot(player, windowId, i)
                }
            }
        }

        fun getInventoryType(inventoryType: InventoryType, size: Int): Int = when (inventoryType) {
            CHEST ->
                when (size) {
                    9 -> 0
                    18 -> 1
                    27 -> 2
                    36 -> 3
                    45 -> 4
                    else -> 5
                }
            DISPENSER, DROPPER -> 6
            FURNACE -> 13
            WORKBENCH, CRAFTING -> 11
            ENCHANTING -> 12
            BREWING -> 10
            MERCHANT -> 18
            ENDER_CHEST, BARREL -> 2
            ANVIL -> 7
            BEACON -> 8
            HOPPER -> 15
            SHULKER_BOX -> 19
            BLAST_FURNACE -> 9
            LECTERN -> 16
            SMOKER -> 20
            LOOM -> 17
            CARTOGRAPHY -> 21
            GRINDSTONE -> 14
            STONECUTTER -> 22
            else -> 6
        }

        fun getClickType(rawType: Any, button: Int, slot: Int): InvClickType = when (INSTANCE.getClickTypeIndex(rawType)) {
            0 -> if (button == 0) LEFT else RIGHT
            1 -> if (button == 0) SHIFT_LEFT else SHIFT_RIGHT
            2 -> if (button == 40) OFFHAND else InvClickType.valueOf("NUMBER_KEY${if (button >= 0) "_${(button + 1)}" else ""}")
            3 -> MIDDLE
            4 -> when (button) {
                0 -> if (slot >= 0) DROP else WINDOW_BORDER_LEFT
                else -> if (slot >= 0) CONTROL_DROP else WINDOW_BORDER_RIGHT
            }
            5 -> when (button) {
                0, 1, 2 -> LEFT
                4, 5, 6 -> RIGHT
                8, 9, 10 -> MIDDLE
                else -> ALL
            }
            6 -> DOUBLE_CLICK
            else -> ALL
        }

        fun sendClearNonIconSlots(player: Player, session: MenuSession) {
            session.menu?.let {
                val layout = session.layout!!
                val hasSlots = it.getOccupiedSlots(player, session.page)
                for (i in 0 until layout.size()) if (!hasSlots.contains(i)) sendRemoveSlot(player, Sessions.TRMENU_WINDOW_ID, i)
            }
        }

        fun sendPacket(player: Player, packetClass: Class<*>, packet: Any, fields: Map<String, Any?>) {
            fields.forEach { SimpleReflection.setFieldValue(packetClass, packet, it.key, it.value) }
            sendPacket(player, packet)
        }

        fun sendPacket(player: Player, packet: Any) = TPacketHandler.sendPacket(player, packet)

    }

}