package cc.trixey.mc.invero.impl.container.window

import cc.trixey.mc.invero.impl.container.InveroManager
import cc.trixey.mc.invero.*
import cc.trixey.mc.invero.window.BaseWindow
import cc.trixey.mc.invero.window.BaseWindowBukkit
import cc.trixey.mc.invero.util.findPanel
import cc.trixey.mc.invero.impl.container.panel.IOStoragePanel
import cc.trixey.mc.invero.impl.container.util.nms.handler
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.PlayerInventory
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import java.util.ArrayList

/**
 * @author Arasple
 * @since 2022/10/29 16:23
 *
 * 一个标准的容器窗口
 * 支持玩家的背包物品的交互，但不支持在玩家背包容器使用 Panel
 */
open class ContainerWindow(
    viewer: Player,
    final override val type: WindowType,
    title: String,
    var lock: Boolean = true
) : BaseWindowBukkit(viewer.uniqueId) {

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
     * 容器规模
     */
    override val scale by lazy {
        ScaleView(type.scaleView.width to type.scaleView.height)
    }

    /**
     * 容器对
     */
    override val inventorySet: InventorySet by lazy {
        WindowInventory(this, null)
    }

    /**
     * 开启窗口
     */
    override fun open() {
        if (isPrimaryThread) inventorySet.open(getViewer())
        else submit { inventorySet.open(getViewer()) }
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
        else findPanel(e.rawSlot)?.handleClick(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * 处理拖拽事件
     */
    override fun handleDrag(e: InventoryDragEvent) {
        if (e.inventory is PlayerInventory) e.isCancelled = lock
        else findPanel(e.inventorySlots)?.handleDrag(this, e) ?: run { e.isCancelled = true }
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
        } else findPanel(e.rawSlot)?.handleItemsMove(this, e) ?: run { e.isCancelled = true }
    }

    override fun isViewing(): Boolean {
        return getViewerSafe()?.let {
            val holder = it.openInventory.topInventory.holder
            holder is WindowHolder && holder.window == this
        } ?: false
    }

    /**
     * 处理物品快速收集事件
     */
    override fun handleItemsCollect(e: InventoryClickEvent) {
        findPanel(e.rawSlot)?.handleItemsCollect(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * @see BaseWindow.getChildren
     */
    override fun getChildren(): ArrayList<Panel> {
        return panels
    }

    /**
     * @see BaseWindow.getParent
     */
    override fun getParent(): Parentable? {
        return null
    }

}