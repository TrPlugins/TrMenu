package cc.trixey.mc.trmenu.legacy.invero.nms

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/10/20
 */
abstract class NMS {

    /**
     * This is sent to the client when it should open an inventory,
     * such as a chest, workbench, or furnace.
     * This message is not sent anywhere for clients opening their own inventory. For horses, use Open Horse Window.
     *
     * A unique id number for the window to be displayed. Notchian server implementation is a counter, starting at 1.
     *
     * @param type The window type to use for display. See WindowProperty for the different values.
     * @param title The title of the window
     */
    abstract fun sendWindowOpen(player: Player, containerId: Int, type: WindowProperty, title: String)

    /**
     * This packet is sent by the client when closing a window.
     * Notchian clients send a Close Window packet with Window ID 0 to close their inventory even though
     * there is never an Open Window packet for the inventory.
     *
     * This is the ID of the window that was closed. 0 for player inventory.
     */
    abstract fun sendWindowClose(player: Player, containerId: Int)

    /**
     * Sent by the server when items in multiple slots (in a window) are added/removed.
     * This includes the main inventory, equipped armour and crafting slots.
     *
     * The ID of window which items are being sent for. 0 for player inventory.
     *
     * @param itemStacks Array of Slot
     */
    abstract fun sendWindowItems(player: Player, containerId: Int, itemStacks: List<ItemStack?>)

    /**
     * Sent by the server when an item in a slot (in a window) is added/removed.
     *
     * The window which is being updated. 0 for player inventory.
     * Note that all known window types include the player inventory.
     * This packet will only be sent for the currently opened window while the player is performing actions,
     * even if it affects the player inventory.
     * After the window is closed, a number of these packets are sent to update the player's inventory window (0).
     *
     * @param slot The slot that should be updated
     * @param itemStack The to update item stack
     */
    abstract fun sendWindowSetSlot(
        player: Player, containerId: Int, slot: Int, itemStack: ItemStack? = null, stateId: Int = 1
    )

    /**
     * https://wiki.vg/Protocol#Window_Property
     *
     * TODO("This packet is used to inform the client that part of a GUI window should be updated.")
     * TODO("完善此结构可以实现信标，熔炉等虚拟UI页面的客户端动效")
     */
    abstract fun sendWindowUpdateData(player: Player, containerId: Int, property: Int, value: Int)

    abstract fun asCraftMirror(itemStack: Any): Any

}