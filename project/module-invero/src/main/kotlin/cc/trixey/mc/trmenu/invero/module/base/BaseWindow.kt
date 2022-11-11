package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.entity.Player
import org.bukkit.event.inventory.*
import taboolib.common.platform.function.getProxyPlayer
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 11:34
 */
abstract class BaseWindow(val viewer: UUID) : Window {

    override val panels: LinkedList<Panel> = LinkedList()

    abstract fun handleClick(e: InventoryClickEvent)

    abstract fun handleDrag(e: InventoryDragEvent)

    abstract fun handleItemsMove(e: InventoryClickEvent)

    abstract fun handleItemsCollect(e: InventoryClickEvent)

    abstract fun handleOpen(e: InventoryOpenEvent)

    abstract fun handleClose(e: InventoryCloseEvent)

    /**
     * Get the viewer of this window
     *
     * @throws NullPointerException if Viewer is null
     */
    fun getViewer(): Player {
        return getViewerSafe() ?: throw NullPointerException("Expected viewer is not valid")
    }

    override fun getViewerSafe(): Player? {
        return getProxyPlayer(viewer)?.castSafely()
    }


    /**
     * Render all panels at once
     */
    override fun render(clearance: Boolean) {
        if (!hasViewer()) throw IllegalStateException("Unable to render this panel while the viewer does not exists")
        if (clearance) pairedInventory.clear()

        // forEach Panels at its weight from low to high
        panels.sortedBy { it.weight.value }.forEach { panel -> panel.render(this) }
    }

    fun findPanelHandler(slot: Int): Panel? {
        return panels.sortedByDescending { it.weight }.firstOrNull {
            it.getClaimedSlots(this).contains(slot)
        }
    }

    override fun handleEvent(e: InventoryEvent) {
        when (e) {
            is InventoryOpenEvent -> handleOpen(e)
            is InventoryCloseEvent -> handleClose(e)
            is InventoryDragEvent -> {
                e.isCancelled = true
                handleDrag(e)
            }

            is InventoryClickEvent -> {
                e.isCancelled = true
                when (e.action) {
                    InventoryAction.MOVE_TO_OTHER_INVENTORY -> handleItemsMove(e)
                    InventoryAction.COLLECT_TO_CURSOR -> handleItemsCollect(e)
                    else -> handleClick(e)
                }
            }
        }
    }

}