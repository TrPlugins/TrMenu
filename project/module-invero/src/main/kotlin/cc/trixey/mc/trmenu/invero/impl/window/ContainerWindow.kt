package cc.trixey.mc.trmenu.invero.impl.window

import cc.trixey.mc.trmenu.invero.impl.WindowHolder.Companion.register
import cc.trixey.mc.trmenu.invero.impl.WindowHolder.Companion.unregister
import cc.trixey.mc.trmenu.invero.impl.panel.IOStoragePanel
import cc.trixey.mc.trmenu.invero.module.PairedInventory
import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Parentable
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.module.base.BaseWindow
import cc.trixey.mc.trmenu.invero.util.handler
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
 * A standard inventory window
 * Player's inventory will not be used or influenced
 */
open class ContainerWindow(
    viewer: Player, override val type: TypeAddress, title: String, var lock: Boolean = true
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
    override val inventorySet by lazy {
        PairedInventory(this, null)
    }

    /**
     * Open this window
     */
    override fun open() {
        getViewer().openInventory(inventorySet.container)
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
        if (e.clickedInventory is PlayerInventory) e.isCancelled = lock
        else findPanelHandler(e.rawSlot)?.handleClick(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * Handle drag event to this panel
     */
    override fun handleDrag(e: InventoryDragEvent) {
        if (e.inventory is PlayerInventory) e.isCancelled = lock
        else findPanelHandler(e.inventorySlots)?.handleDrag(this, e) ?: run { e.isCancelled = true }
    }

    /**
     * Handle items move event concerns this panel
     *
     * TODO 不可控现象: 无法取得目标 slot, 无法判断目标 panel, 从而可能使 item 移动到非 IOPanel 上..
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
     * Handle items collect event concerns this panel
     */
    override fun handleItemsCollect(e: InventoryClickEvent) {
        findPanelHandler(e.rawSlot)?.handleItemsCollect(this, e) ?: run { e.isCancelled = true }
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