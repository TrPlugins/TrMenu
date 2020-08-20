package me.arasple.mc.trmenu.api.nms.impl

import io.izzel.taboolib.Version
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.api.nms.NMS
import net.minecraft.server.v1_16_R1.*
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/8/19 12:10
 */
class NMSImpl : NMS() {

    override fun sendOpenWindow(player: Player, windowId: Int, inventoryType: InventoryType, size: Int, inventoryTitle: String) {
        if (Version.isAfter(Version.v1_13)) {
            sendPacket(
                player,
                PacketPlayOutOpenWindow(),
                Pair("a", windowId),
                Pair("b", getInventoryType(inventoryType, size)),
                Pair("c", ChatComponentText(inventoryTitle))
            )
        } else {
            sendPacket(
                player,
                net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow(
                    windowId,
                    "minecraft:${inventoryType.name.toLowerCase()}",
                    net.minecraft.server.v1_12_R1.ChatComponentText(inventoryTitle),
                    size
                )
            )
        }
    }

    override fun sendCloseWindow(player: Player, windowId: Int) {
        sendPacket(player, PacketPlayOutCloseWindow(windowId))
    }

    override fun sendOutSlot(player: Player, windowId: Int, slot: Int, itemStack: ItemStack) {
        sendPacket(
            player,
            PacketPlayOutSetSlot(),
            Pair("a", windowId),
            Pair("b", slot),
            Pair("c", asNMSItem(itemStack))
        )
    }

    override fun sendRemoveSlot(player: Player, windowId: Int, slot: Int) {
        sendPacket(
            player,
            PacketPlayOutSetSlot(),
            Pair("a", windowId),
            Pair("b", slot)
        )
    }

    override fun clearInventory(player: Player, startSlot: Int, windowId: Int) {
        val session = player.getMenuSession()
        val slots = session.menu?.getOccupiedSlots(player, session.page) ?: return

        for (i in (startSlot..startSlot + 35).filter { !slots.contains(it) }) {
            sendRemoveSlot(player, windowId, i)
        }
    }

    override fun asNMSItem(itemStack: ItemStack): Any {
        return CraftItemStack.asNMSCopy(itemStack)
    }

    override fun asBukkitItem(itemStack: Any?): ItemStack? {
        return if (itemStack == null) null else CraftItemStack.asBukkitCopy(itemStack as net.minecraft.server.v1_16_R1.ItemStack?)
    }

    override fun getClickTypeIndex(clickType: Any): Int {
        return if (Version.isAfter(Version.v1_9)) InventoryClickType.values().indexOf(clickType) else clickType as Int
    }

    override fun getPlayerTexture(player: Player): String {
        return (player as CraftPlayer).profile.properties["textures"].iterator().let {
            return@let if (it.hasNext()) it.next().value else ""
        }
    }

}