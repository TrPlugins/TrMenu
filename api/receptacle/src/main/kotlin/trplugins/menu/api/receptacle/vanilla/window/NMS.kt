package trplugins.menu.api.receptacle.vanilla.window

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/12/4 21:20
 */
abstract class NMS {


    /**
     * This packet is sent by the client when closing a window.
     * Notchian clients send a Close Window packet with Window ID 0 to close their inventory even though
     * there is never an Open Window packet for the inventory.
     *
     * This is the ID of the window that was closed. 0 for player inventory.
     */
    abstract fun sendWindowsClose(player: Player, windowId: Int = 119)

    /**
     * Sent by the server when items in multiple slots (in a window) are added/removed.
     * This includes the main inventory, equipped armour and crafting slots.
     *
     * The ID of window which items are being sent for. 0 for player inventory.
     *
     * @param items Array of Slot
     */
    abstract fun sendWindowsItems(player: Player, windowId: Int = 119, items: Array<ItemStack?>)

    /**
     * This is sent to the client when it should open an inventory,
     * such as a chest, workbench, or furnace.
     * This message is not sent anywhere for clients opening their own inventory. For horses, use Open Horse Window.
     *
     * A unique id number for the window to be displayed. Notchian server implementation is a counter, starting at 1.
     *
     * @param type The window type to use for display. See ReceptacleType for the different values.
     * @param title The title of the window
     */
    abstract fun sendWindowsOpen(player: Player, windowId: Int = 119, type: WindowLayout, title: String)

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
    abstract fun sendWindowsSetSlot(player: Player, windowId: Int = 119, slot: Int, itemStack: ItemStack? = null, stateId: Int = 1)

    /**
     * https://wiki.vg/Protocol#Window_Property
     *
     * TODO This packet is used to inform the client that part of a GUI window should be updated.
     */
    abstract fun sendWindowsUpdateData(player: Player, windowId: Int = 119, property: Int, value: Int)

}