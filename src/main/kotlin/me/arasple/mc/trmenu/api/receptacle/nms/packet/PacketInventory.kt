package me.arasple.mc.trmenu.api.receptacle.nms.packet

import me.arasple.mc.trmenu.api.receptacle.nms.NMS
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/12/4 21:22
 */
interface PacketInventory {

    fun send(player: Player) = NMS.INSTANCE.sendInventoryPacket(player, this)

}