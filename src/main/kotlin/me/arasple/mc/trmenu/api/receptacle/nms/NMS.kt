package me.arasple.mc.trmenu.api.receptacle.nms

import com.mojang.authlib.GameProfile
import io.izzel.taboolib.kotlin.Reflex
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.packet.TPacketHandler
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketInventory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/12/4 21:20
 */
abstract class NMS {

    companion object {

        /**
         * @see NMSImpl
         */
        @TInject(asm = "me.arasple.mc.trmenu.api.receptacle.nms.NMSImpl")
        lateinit var INSTANCE: NMS

    }

    abstract fun sendInventoryPacket(player: Player, vararg packets: PacketInventory)

    abstract fun toChatBaseComponent(string: String, craftChatMessage: Boolean = true): Any

    abstract fun toNMSItemStack(vararg itemStack: ItemStack?): Any

    fun sendPacket(player: Player, packet: Any, vararg fields: Pair<Any, Any>) {
        TPacketHandler.sendPacket(player, Reflex.of(packet).let { inst ->
            fields.forEach { inst.set(it.first.toString(), it.second) }
            inst.instance
        })
    }

    abstract fun getGameProfile(player: Player): GameProfile
}