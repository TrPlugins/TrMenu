package me.arasple.mc.trmenu.nms.impl;

import io.izzel.taboolib.util.chat.ComponentSerializer;
import io.izzel.taboolib.util.chat.TextComponent;
import me.arasple.mc.trmenu.nms.InvTitler;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * @author Arasple
 * @date 2020/1/20 14:24
 * for versions below Minecraft 1.14
 */
public class InvTitlerOlder extends InvTitler {

    @Override
    public void setInventoryTitle(Player player, Inventory inventory, String title) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(
                handle.activeContainer.windowId,
                getByInventory(inventory),
                IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(TextComponent.fromLegacyText(title)))
        );
        handle.playerConnection.sendPacket(packet);
        handle.updateInventory(handle.activeContainer);
    }

    private String getByInventory(Inventory inventory) {
        InventoryType type = inventory.getType();
        int size = type.getDefaultSize();

        if (type == InventoryType.CHEST) {
            if (size == 9) {
                return "minecraft:generic_9x1";
            } else if (size == 18) {
                return "minecraft:generic_9x2";
            } else if (size == 27) {
                return "minecraft:generic_9x3";
            } else if (size == 36) {
                return "minecraft:generic_9x4";
            } else if (size == 45) {
                return "minecraft:generic_9x5";
            } else {
                return "minecraft:generic_9x6";
            }
        } else if (type == InventoryType.DROPPER || type == InventoryType.DISPENSER) {
            return "minecraft:generic_3x3";
        } else if (type == InventoryType.BARREL || type == InventoryType.ENDER_CHEST) {
            return "minecraft:generic_9x3";
        } else {
            return "minecraft:" + type.name().toLowerCase();
        }
    }

}
