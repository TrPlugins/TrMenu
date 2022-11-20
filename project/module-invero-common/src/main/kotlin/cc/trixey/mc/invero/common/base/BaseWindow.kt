package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.Window
import org.bukkit.entity.Player
import org.bukkit.event.inventory.*
import taboolib.common.platform.function.getProxyPlayer
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 11:34
 * @see Window
 *
 * 基础的 Window 抽象实例
 */
abstract class BaseWindow(val viewer: UUID) : Window {

    /**
     * 该窗口使用的 Panels
     */
    override val panels: ArrayList<Panel> = ArrayList()

    operator fun plusAssign(value: Panel) {
        panels += value
    }

    operator fun minusAssign(value: Panel) {
        panels -= value
    }

    override fun handleEvent(e: InventoryEvent) {
        when (e) {
            is InventoryOpenEvent -> handleOpen(e)
            is InventoryCloseEvent -> handleClose(e)
            is InventoryDragEvent -> handleDrag(e)
            is InventoryClickEvent -> {
                when (e.action) {
                    InventoryAction.MOVE_TO_OTHER_INVENTORY -> handleItemsMove(e)
                    InventoryAction.COLLECT_TO_CURSOR -> handleItemsCollect(e)
                    else -> handleClick(e)
                }
            }
        }
    }

    abstract fun handleClick(e: InventoryClickEvent)

    abstract fun handleDrag(e: InventoryDragEvent)

    abstract fun handleItemsMove(e: InventoryClickEvent)

    abstract fun handleItemsCollect(e: InventoryClickEvent)

    abstract fun handleOpen(e: InventoryOpenEvent)

    abstract fun handleClose(e: InventoryCloseEvent)

    fun getViewer(): Player {
        return getViewerSafe() ?: throw NullPointerException("Expected viewer is not valid")
    }

    override fun getViewerSafe(): Player? {
        return getProxyPlayer(viewer)?.castSafely()
    }

    override fun renderWindow(clearance: Boolean) {
        if (!hasViewer()) error("Unable to render this panel while the viewer does not exists")
        if (clearance) inventorySet.clear()

        // TODO 改进渲染逻辑
        // 重叠区域、不必要渲染区域省去
        panels.sortedBy { it.weight.value }.forEach { panel ->
            panel.renderPanel()
        }
    }

    fun findPanelHandler(slot: Int): Panel? {
        return panels.sortedByDescending { it.weight }.firstOrNull {
            it.getSlotsMap(this).absoluteSlots.contains(slot)
        }
    }

    // TODO ??
    fun findPanelHandler(slots: Collection<Int>): Panel? {
        return panels.sortedByDescending { it.weight }.firstOrNull {
            it.getSlotsMap(this).absoluteSlots.containsAll(slots)
        }
    }

}