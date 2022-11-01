package cc.trixey.mc.trmenu.invero.util

import cc.trixey.mc.trmenu.invero.module.Window
import net.minecraft.server.v1_16_R3.ChatComponentText
import net.minecraft.server.v1_16_R3.PacketPlayOutCloseWindow
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow
import net.minecraft.world.inventory.Container
import net.minecraft.world.inventory.Containers
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage
import org.bukkit.entity.Player

import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.MinecraftVersion.majorLegacy

/**
 * @author Arasple
 * @since 2022/10/29 16:29
 */
class NMSImpl : NMS() {

    override fun getPlayerWindowId(player: Player): Int {
        player as CraftPlayer

        return if (isUniversal) {
            player.handle.getProperty<Container>("containerMenu")!!.getProperty<Int>("containerId")!!
        } else {
            player.handle.activeContainer.windowId
        }
    }

    override fun updateWindowTitle(player: Player, window: Window, title: String) {
        val containerId = getPlayerWindowId(player)
        val type = window.type
        val instance = PacketPlayOutOpenWindow::class.java.unsafeInstance()

        when {
            isUniversal -> {
                player.postPacket(
                    instance,
                    "containerId" to containerId,
                    if (majorLegacy < 11900) "type" to type.serialId
                    else "type" to Containers::class.java.getProperty<Containers<*>>(type.vanillaId, true),
                    "title" to CraftChatMessage.fromStringOrNull(title)
                )
            }

            majorLegacy >= 11400 -> {
                player.postPacket(
                    instance, "a" to containerId, "b" to type.serialId, "c" to CraftChatMessage.fromStringOrNull(title)
                )
            }

            else -> {
                player.postPacket(
                    instance,
                    "a" to containerId,
                    "b" to type.bukkitId,
                    "c" to ChatComponentText(title),
                    "d" to type.containerSize - 1 // Fixed ViaVersion can not view 6x9 menu bug.
                )
            }
        }

        player.updateInventory()
    }

    override fun closeWindow(player: Player) {
        val containerId = getPlayerWindowId(player)

        player.postPacket(PacketPlayOutCloseWindow(containerId))
    }

}