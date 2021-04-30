package me.arasple.mc.trmenu.api.receptacle

import io.izzel.taboolib.module.packet.Packet
import io.izzel.taboolib.module.packet.TPacket
import me.arasple.mc.trmenu.api.event.ReceptacleInteractEvent
import me.arasple.mc.trmenu.api.receptacle.manager.Manager
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowClose
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowSetSlot
import me.arasple.mc.trmenu.api.receptacle.window.Receptacle
import me.arasple.mc.trmenu.api.receptacle.window.ReceptacleType
import me.arasple.mc.trmenu.api.receptacle.window.type.InventoryChest
import me.arasple.mc.trmenu.api.receptacle.window.vanilla.ClickType
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.*

/**
 * @author Arasple
 * @date 2021/1/17 11:03
 */
object ReceptacleAPI {

    val MANAGER = Manager()

    fun createReceptacle(inventoryType: InventoryType, title: String = "Def"): Receptacle {
        if (inventoryType == CHEST) return InventoryChest()

        val receptacleType = when (inventoryType) {
            ENDER_CHEST, BARREL -> ReceptacleType.GENERIC_9X3
            DISPENSER, DROPPER -> ReceptacleType.GENERIC_3X3
            ANVIL -> ReceptacleType.ANVIL
            FURNACE -> ReceptacleType.FURNACE
            WORKBENCH, CRAFTING -> ReceptacleType.CRAFTING
            ENCHANTING -> ReceptacleType.ENCHANTMENT_TABLE
            BREWING -> ReceptacleType.BREWING_STAND
            MERCHANT -> ReceptacleType.MERCHANT
            BEACON -> ReceptacleType.BEACON
            HOPPER -> ReceptacleType.HOPPER
            SHULKER_BOX -> ReceptacleType.SHULKER_BOX
            BLAST_FURNACE -> ReceptacleType.BLAST_FURNACE
            SMOKER -> ReceptacleType.SMOKER
            LOOM -> ReceptacleType.LOOM
            CARTOGRAPHY -> ReceptacleType.CARTOGRAPHY
            GRINDSTONE -> ReceptacleType.GRINDSTONE
            STONECUTTER -> ReceptacleType.STONE_CUTTER
            else -> throw IllegalArgumentException("Do not support $inventoryType")
        }

        return Receptacle(inventoryType, receptacleType, title)
    }

    @TPacket(type = TPacket.Type.RECEIVE)
    private fun e(player: Player, packet: Packet): Boolean {
        val receptacle = MANAGER.getViewingReceptacle(player) ?: return true

        if (packet.`is`("PacketPlayInWindowClick") && packet.read("a", -1) == 119) {
            val slot = packet.read("slot") as Int
            val mode = packet.read("shift").toString()
            val button = packet.read("button") as Int
            val clickType = ClickType.from(mode, button, slot) ?: return true
            val evt = ReceptacleInteractEvent(player, receptacle, clickType, slot).call()
            receptacle.callEventClick(player, evt)

            if (evt.isCancelled) PacketWindowSetSlot(-1, -1).send(player)

            return false
        } else if (packet.`is`("PacketPlayInCloseWindow") && packet.read("id", -1) == 119) {
            receptacle.close(player, false)

            // 防止关闭菜单后, 动态标题频率过快出现的卡假容器
            Tasks.delay(1L, true) {
                MANAGER.getViewingReceptacle(player) ?: kotlin.run {
                    player.updateInventory()
                }
            }
            Tasks.delay(4L, true) {
                MANAGER.getViewingReceptacle(player) ?: kotlin.run {
                    PacketWindowClose().send(player)
                }
            }

            return false
        }

        return true
    }

}