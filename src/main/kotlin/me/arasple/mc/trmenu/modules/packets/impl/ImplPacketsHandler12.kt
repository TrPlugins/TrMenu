package me.arasple.mc.trmenu.modules.packets.impl

import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import net.minecraft.server.v1_12_R1.*
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/29 21:23
 */
class ImplPacketsHandler12 : PacketsHandler() {

    override fun sendOpenWindow(player: Player, windowId: Int, inventoryType: InventoryType, size: Int, inventoryTitle: String) = sendPacket(
        player,
        PacketPlayOutOpenWindow::class.java,
        PacketPlayOutOpenWindow(),
        mapOf(
            Pair("a", windowId),
            Pair("b", "minecraft:" + inventoryType.name.toLowerCase()),
            Pair("c", ChatComponentText(inventoryTitle)),
            Pair("d", size)
        )
    )

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