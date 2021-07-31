package me.arasple.mc.trmenu.api.receptacle.nms

import com.mojang.authlib.GameProfile
import io.izzel.taboolib.kotlin.Reflex
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketInventory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacket

/**
 * @author Arasple
 * @date 2020/12/4 21:20
 */
abstract class NMS {

    companion object {

        /**
         * @see NMSImpl
         */
        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }

    }

    abstract fun sendInventoryPacket(player: Player, vararg packets: PacketInventory)

    abstract fun toChatBaseComponent(string: String, craftChatMessage: Boolean = true): Any

    abstract fun toNMSItemStack(vararg itemStack: ItemStack?): Any

    fun sendPacket(player: Player, packet: Any, vararg fields: Pair<Any, Any>) {
        player.sendPacket(Reflex.of(packet).let { inst ->
            fields.forEach { inst.set(it.first.toString(), it.second) }
            inst.instance
        }!!)
    }

    abstract fun getGameProfile(player: Player): GameProfile
}