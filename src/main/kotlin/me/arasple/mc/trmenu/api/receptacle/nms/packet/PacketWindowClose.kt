package me.arasple.mc.trmenu.api.receptacle.nms.packet

/**
 * @author Arasple
 * @date 2020/12/4 21:22
 *
 * This packet is sent by the client when closing a window.
 * Notchian clients send a Close Window packet with Window ID 0 to close their inventory even though
 * there is never an Open Window packet for the inventory.
 *
 * @param windowId This is the ID of the window that was closed. 0 for player inventory.
 *
 */
inline class PacketWindowClose(val windowId: Int = 119) : PacketInventory