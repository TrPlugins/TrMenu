package me.arasple.mc.trmenu.api.receptacle.nms

import com.mojang.authlib.GameProfile
import io.izzel.taboolib.Version
import me.arasple.mc.trmenu.api.receptacle.nms.packet.*
import net.minecraft.server.v1_12_R1.IChatBaseComponent
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot
import net.minecraft.server.v1_16_R3.ChatComponentText
import net.minecraft.server.v1_16_R3.PacketPlayOutCloseWindow
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow
import net.minecraft.server.v1_16_R3.PacketPlayOutWindowItems
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/12/4 21:25
 */
class NMSImpl : NMS() {

    private val emptyItemStack = CraftItemStack.asNMSCopy((ItemStack(Material.AIR)))

    override fun sendInventoryPacket(player: Player, vararg packets: PacketInventory) {
        packets.forEach {
            when (it) {
                // Open Window Packet
                is PacketWindowOpen -> {
                    if (Version.isAfter(Version.v1_13)) {
                        sendPacket(
                            player,
                            PacketPlayOutOpenWindow(),
                            'a' to it.windowId,
                            'b' to it.type.vanillaId,
                            'c' to toChatBaseComponent(it.title)
                        )
                    } else {
                        sendPacket(
                            player,
                            net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow(
                                it.windowId,
                                it.type.vanillaType,
                                toChatBaseComponent(it.title) as IChatBaseComponent?,
                                it.type.containerSize
                                // entityId
                            )
                        )
                    }
                }
                // Close Window Packet
                is PacketWindowClose -> sendPacket(player, PacketPlayOutCloseWindow(it.windowId))
                // Update Window Items
                is PacketWindowItems -> {
                    sendPacket(
                        player,
                        PacketPlayOutWindowItems(),
                        'a' to it.windowId,
                        'b' to toNMSItemStack(*it.items)
                    )
                }
                // Update Window Slot
                is PacketWindowSetSlot -> {
                    sendPacket(
                        player,
                        PacketPlayOutSetSlot(),
                        'a' to it.windowId,
                        'b' to it.slot,
                        'c' to asNMSCopy(it.itemStack)
                    )
                }
            }
        }
    }

    override fun toChatBaseComponent(string: String, craftChatMessage: Boolean): Any {
        return if (craftChatMessage) CraftChatMessage.fromString(string).first()
        else ChatComponentText(string)
    }

    override fun toNMSItemStack(vararg itemStack: ItemStack?): Any {
        if (itemStack.size > 1) {
            return itemStack.map { asNMSCopy(it) }
        }
        return asNMSCopy(itemStack[0])
    }

    override fun getGameProfile(player: Player): GameProfile {
        return (player as CraftPlayer).profile
    }

    private fun asNMSCopy(itemStack: ItemStack?): Any {
        return if (itemStack == null || itemStack.type == Material.AIR) emptyItemStack
        else CraftItemStack.asNMSCopy(itemStack)
    }

}