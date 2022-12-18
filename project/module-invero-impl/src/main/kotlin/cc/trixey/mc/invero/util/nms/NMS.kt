package cc.trixey.mc.invero.util.nms

import cc.trixey.mc.invero.common.Window
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/10/29 16:29
 */
abstract class NMS {

    /**
     * Get the containerID of current InventoryView of a player
     */
    abstract fun getPlayerWindowId(player: Player): Int

    /**
     * Update the window's title
     */
    abstract fun updateWindowTitle(player: Player, window: Window, title: String)

    /**
     * Update the window's title
     */
    abstract fun closeWindow(player: Player)

}