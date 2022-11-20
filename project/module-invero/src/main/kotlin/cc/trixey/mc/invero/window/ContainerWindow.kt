package cc.trixey.mc.invero.window

import cc.trixey.mc.invero.InveroManager
import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.Parentable
import cc.trixey.mc.invero.common.WindowInventory
import cc.trixey.mc.invero.common.WindowType
import cc.trixey.mc.invero.common.base.BaseWindow
import cc.trixey.mc.invero.panel.IOStoragePanel
import cc.trixey.mc.invero.util.nms.handler
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.PlayerInventory

/**
 * @author Arasple
 * @since 2022/10/29 16:23
 *
 * 一个标准的容器窗口
 * 支持玩家的背包物品的交互，但不支持在玩家背包容器使用 Panel
 */
open class ContainerWindow(
    viewer: Player,
    override val type: WindowType,
    title: String,
    var lock: Boolean = true
) : BaseWindow(viewer.uniqueId) {


    /**
     * 容器的标题
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
     * 容器对
     */
    override val inventorySet by lazy {
        WindowInventory(this, null)
    }

    /**
     * 开启窗口
     */
    override fun open() {
        getViewer().openInventory(inventorySet.container)
    }

    /**
     * 处理开启事件
     */
    override fun handleOpen(e: InventoryOpenEvent) {
        if (!e.isCancelled) {
            InveroManager.register(this)
        }
        renderWindow()
    }

    /**
     * 处理关闭事件
     */
    override fun handleClose(e: InventoryCloseEvent) {
        InveroManager.unregister(this)
    }


    /**
     * 处理点击事件
     */
    override fun handleClick(e: InventoryClickEvent) {
        if (e.clickedInventory is PlayerInventory) e.isCancelled = lock
        else findPanelHandler(e.rawSlot)?.handleClick(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * 处理拖拽事件
     */
    override fun handleDrag(e: InventoryDragEvent) {
        if (e.inventory is PlayerInventory) e.isCancelled = lock
        else findPanelHandler(e.inventorySlots)?.handleDrag(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * 处理物品位移事件
     */
    override fun handleItemsMove(e: InventoryClickEvent) {
        if (e.inventory is PlayerInventory) {
            e.isCancelled = lock

            /*
            处理从玩家背包 shift_上移 逻辑：

            1 获取需要移动的物品
            2 Window中找到可以接受/合并该物品的IOPanel
            3 模拟操作，并处理完物品
             */

            /*
            处理从 IOPanel Shift_下移 逻辑:

            通常情况下是直接入背包，暂无问题
             */

            if (!lock) {
                val toMove = e.currentItem
                panels.filterIsInstance<IOStoragePanel>().forEach {
                    // 模拟取
                    it.storage
                    // 模拟删
//                            e.currentItem = null
                }
            }
        } else findPanelHandler(e.rawSlot)?.handleItemsMove(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * 处理物品快速收集事件
     */
    override fun handleItemsCollect(e: InventoryClickEvent) {
        findPanelHandler(e.rawSlot)?.handleItemsCollect(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * @see BaseWindow.getChildren
     */
    override fun getChildren(): List<Panel> {
        return panels
    }

    /**
     * @see BaseWindow.getParent
     */
    override fun getParent(): Parentable? {
        return null
    }

}