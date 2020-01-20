package me.arasple.mc.trmenu.nms.impl;

import io.izzel.taboolib.util.chat.ComponentSerializer;
import io.izzel.taboolib.util.chat.TextComponent;
import me.arasple.mc.trmenu.nms.InvTitler;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * @author Arasple
 * @date 2020/1/20 14:24
 * for Minecraft 1.14+
 */
public class InvTitlerModern extends InvTitler {

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

    private Containers<?> getByInventory(Inventory inventory) {
        InventoryType type = inventory.getType();
        int size = inventory.getSize();
        if (type == InventoryType.CHEST) {
            if (size == 9) {
                return Containers.GENERIC_9X1;
            } else if (size == 18) {
                return Containers.GENERIC_9X2;
            } else if (size == 27) {
                return Containers.GENERIC_9X3;
            } else if (size == 36) {
                return Containers.GENERIC_9X4;
            } else if (size == 45) {
                return Containers.GENERIC_9X5;
            } else {
                return Containers.GENERIC_9X6;
            }
        } else if (type == InventoryType.DROPPER || type == InventoryType.DISPENSER) {
            return Containers.GENERIC_3X3;
        } else if (type == InventoryType.BARREL || type == InventoryType.ENDER_CHEST) {
            return Containers.GENERIC_9X3;
        } else if (type == InventoryType.CRAFTING || type == InventoryType.WORKBENCH) {
            return Containers.CRAFTING;
        } else if (type == InventoryType.HOPPER) {
            return Containers.HOPPER;
        } else if (type == InventoryType.LOOM) {
            return Containers.LOOM;
        } else if (type == InventoryType.ANVIL) {
            return Containers.ANVIL;
        } else if (type == InventoryType.BEACON) {
            return Containers.BEACON;
        } else if (type == InventoryType.SMOKER) {
            return Containers.SMOKER;
        } else if (type == InventoryType.BREWING) {
            return Containers.BREWING_STAND;
        } else if (type == InventoryType.FURNACE) {
            return Containers.FURNACE;
        } else if (type == InventoryType.LECTERN) {
            return Containers.LECTERN;
        } else if (type == InventoryType.MERCHANT) {
            return Containers.MERCHANT;
        } else if (type == InventoryType.ENCHANTING) {
            return Containers.ENCHANTMENT;
        } else if (type == InventoryType.GRINDSTONE) {
            return Containers.GRINDSTONE;
        } else if (type == InventoryType.CARTOGRAPHY) {
            return Containers.CARTOGRAPHY_TABLE;
        } else if (type == InventoryType.SHULKER_BOX) {
            return Containers.SHULKER_BOX;
        } else if (type == InventoryType.STONECUTTER) {
            return Containers.STONECUTTER;
        } else if (type == InventoryType.BLAST_FURNACE) {
            return Containers.BLAST_FURNACE;
        } else {
            return Containers.GENERIC_3X3;
        }
    }

}
