package cc.trixey.mc.trmenu.invero.module

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

    override fun getViewerSafe(): Player? {
        return getProxyPlayer(viewer)?.castSafely()
    }

    /**
     * Get the viewer of this window
     *
     * @throws NullPointerException if Viewer is null
     */
    fun getViewer(): Player {
        return getProxyPlayer(viewer)?.castSafely() ?: throw NullPointerException("Expected viewer is not valid")
    }


    /**
     * Render all panels at once
     */
    fun render(clearance: Boolean = false) {
        if (!hasViewer()) throw IllegalStateException("Unable to render this panel while the owner Window is not opened by a player")
        if (clearance) pairedInventory.clear()

        // forEach Panels at its weight from low to high
        panels.sortedBy { it.weight.value }.forEach { panel ->
            panel.postRender {
                panel.elements.forEach { (relSlot, element) ->
                    slotsMap[relSlot]?.let { absSlot ->
                        pairedInventory[absSlot] = element.renderItem()
                    }
                }
            }
        }
    }

    override fun handleEvent(e: InventoryEvent) {
        when (e) {
            is InventoryDragEvent -> handleDrag(e)
            is InventoryOpenEvent -> handleOpen(e)
            is InventoryCloseEvent -> handleClose(e)
            is InventoryClickEvent -> {
                when (e.action) {
                    InventoryAction.MOVE_TO_OTHER_INVENTORY -> handleItemsMove(e)
                    InventoryAction.COLLECT_TO_CURSOR -> handleItemsCollect(e)
                    else -> handleClick(e)
                }
            }
        }
    }

}