package cc.trixey.mc.trmenu.invero.impl.window

import cc.trixey.mc.trmenu.invero.impl.WindowHolder.Companion.register
import cc.trixey.mc.trmenu.invero.impl.WindowHolder.Companion.unregister
import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Parentable
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.module.base.BaseWindow
import cc.trixey.mc.trmenu.invero.module.`object`.PairedInventory
import cc.trixey.mc.trmenu.invero.util.handler
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent

/**
 * @author Arasple
 * @since 2022/10/29 16:23
 *
 * A standard inventory window
 * Player's inventory will not be used or influenced
 */
open class ContainerWindow(
    viewer: Player, override val type: TypeAddress, title: String
) : BaseWindow(viewer.uniqueId) {

    /**
     * Title of the container
     * submit {closeWindow} is to reset any left-behind title packet
     */
    override var title = title
        set(value) {
            getViewerSafe()?.let {
                if (!isViewing()) {
                    handler.closeWindow(it)
                } else {
                    handler.updateWindowTitle(it, this, value)
                }
            }
            field = value
        }

    /**
     * Bukkit based inventory of the Window
     */
    override val pairedInventory by lazy {
        PairedInventory(this, null)
    }

    /**
     * Open this window
     */
    override fun open() {
        getViewer().openInventory(pairedInventory.container)
    }

    /**
     * Handle opening event
     */
    override fun handleOpen(e: InventoryOpenEvent) {
        if (!e.isCancelled) register()
        renderWindow()
    }

    /**
     * Handle close event
     */
    override fun handleClose(e: InventoryCloseEvent) {
        unregister()
    }


    /**
     * Handle click event to this panel
     */
    override fun handleClick(e: InventoryClickEvent) {
        findPanelHandler(e.slot)?.handleClick(this, e)
    }

    /**
     * Handle drag event to this panel
     */
    override fun handleDrag(e: InventoryDragEvent) {
        findPanelHandler(*e.inventorySlots.toIntArray())?.handleDrag(this, e)
    }

    /**
     * Handle items move event concerns this panel
     */
    override fun handleItemsMove(e: InventoryClickEvent) {
        findPanelHandler(e.slot)?.handleItemsMove(this, e)
    }

    /**
     * Handle items collect event concerns this panel
     */
    override fun handleItemsCollect(e: InventoryClickEvent) {
        findPanelHandler(e.slot)?.handleItemsCollect(this, e)
    }

    /**
     * Get this window's children
     * In this case, all the panels it contains
     */
    override fun getChildren(): List<Panel> {
        return panels
    }

    /**
     * Get this window's parent
     * In this case, none at all
     */
    override fun getParent(): Parentable? {
        return null
    }

}