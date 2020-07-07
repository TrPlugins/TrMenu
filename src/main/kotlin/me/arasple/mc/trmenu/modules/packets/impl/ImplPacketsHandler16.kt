package me.arasple.mc.trmenu.modules.packets.impl

import io.izzel.taboolib.module.lite.SimpleReflection
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import net.minecraft.server.v1_16_R1.*
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack


/**
 * @author Arasple
 * @date 2020/3/5 19:41
 */
class ImplPacketsHandler16 : PacketsHandler() {

    init {
        SimpleReflection.checkAndSave(
            PacketPlayOutOpenWindow::class.java
        )
    }

    override fun sendOpenWindow(player: Player, windowId: Int, inventoryType: InventoryType, size: Int, inventoryTitle: String) = sendPacket(player, PacketPlayOutOpenWindow::class.java, PacketPlayOutOpenWindow(), mapOf(Pair("a", windowId), Pair("b", getInventoryType(inventoryType, size)), Pair("c", ChatComponentText(inventoryTitle))))

    override fun sendCloseWindow(player: Player, windowId: Int) = sendPacket(player, PacketPlayOutCloseWindow(windowId))

    override fun sendOutSlot(player: Player, windowId: Int, slot: Int, itemStack: ItemStack) = sendPacket(player, PacketPlayOutSetSlot::class.java, PacketPlayOutSetSlot(), mapOf(Pair("a", windowId), Pair("b", slot), Pair("c", asNMSItem(itemStack))))

    override fun sendRemoveSlot(player: Player, windowId: Int, slot: Int) = sendPacket(player, PacketPlayOutSetSlot::class.java, PacketPlayOutSetSlot(), mapOf(Pair("a", windowId), Pair("b", slot), Pair("c", EMPTY_ITEM)))

    override fun clearInventory(player: Player, startSlot: Int, windowId: Int) {
        for (i in startSlot..startSlot + 35) sendRemoveSlot(player, windowId, i)
    }

    override fun asNMSItem(itemStack: ItemStack): Any = CraftItemStack.asNMSCopy(itemStack)

    override fun getClickTypeIndex(clickType: Any): Int = InventoryClickType.values().indexOf(clickType)

    override fun getPlayerTexture(player: Player): String = (player as CraftPlayer).profile.properties["textures"].iterator().next().value

}