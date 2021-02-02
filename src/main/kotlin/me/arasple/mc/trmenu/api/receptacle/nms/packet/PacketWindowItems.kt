package me.arasple.mc.trmenu.api.receptacle.nms.packet

import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/12/4 21:55
 *
 * Sent by the server when items in multiple slots (in a window) are added/removed.
 * This includes the main inventory, equipped armour and crafting slots.
 *
 * @param windowId The ID of window which items are being sent for. 0 for player inventory.
 * @param count Number of elements in the following array
 * @param items Array of Slot
 *
 */
class PacketWindowItems(val windowId: Int = 119, val count: Int, val items: Array<ItemStack?>) : PacketInventory