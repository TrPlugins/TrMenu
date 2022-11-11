package cc.trixey.mc.trmenu.invero.impl.window

import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.module.`object`.PairedInventory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/10/29 16:44
 *
 * A full complete window which uses player's inventory for panels
 * By default player's inventory will be saved before opening, and restored after closure
 */
class CompleteWindow(viewer: Player, type: TypeAddress, title: String) : ContainerWindow(viewer, type, title) {

    /**
     * Backup of the player's items
     */
    val playerItems = arrayOfNulls<ItemStack>(36)

    /**
     * @see PairedInventory
     */
    override val pairedInventory: PairedInventory by lazy {
        PairedInventory(this, viewer)
    }

    /**
     * Handle open event
     */
    override fun handleOpen(e: InventoryOpenEvent) {
        backupPlayerInventory()
        super.handleOpen(e)
    }

    /**
     * Handle close event
     */
    override fun handleClose(e: InventoryCloseEvent) {
        restorePlayerInventory()
        super.handleClose(e)
    }

    /**
     * Backup player's inventory contents
     */
    private fun backupPlayerInventory() {
        pairedInventory.getPlayerInventory().apply {
            storageContents.forEachIndexed { index, itemStack ->
                playerItems[index] = itemStack
            }
            clear()
        }
    }

    /**
     * Restore player's inventory contents
     */
    private fun restorePlayerInventory() {
        pairedInventory.getPlayerInventory().apply {
            playerItems.forEachIndexed { index, itemStack ->
                storageContents[index] = itemStack
            }
        }
    }

}