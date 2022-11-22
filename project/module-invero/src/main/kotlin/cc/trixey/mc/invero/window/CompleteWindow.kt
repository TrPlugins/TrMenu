package cc.trixey.mc.invero.window

import cc.trixey.mc.invero.common.InventorySet
import cc.trixey.mc.invero.common.WindowInventory
import cc.trixey.mc.invero.common.WindowType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/10/29 16:44
 *
 * 一个完整的容器窗口
 * 允许在玩家背包容器中使用 Panel，但不允许玩家背包物品的交互
 *
 * 默认情况下，开启时会备份玩家背包并清空，关闭后复原
 */
class CompleteWindow(viewer: Player, type: WindowType, title: String) : ContainerWindow(viewer, type, title) {

    /**
     * @see WindowInventory
     */
    override val inventorySet: InventorySet by lazy {
        WindowInventory(this, viewer)
    }

    /**
     * 玩家物品的备份
     */
    var playerItems = arrayOfNulls<ItemStack>(36)

    /**
     * 处理开启事件
     */
    override fun handleOpen(e: InventoryOpenEvent) {
        backupPlayerInventory()
        super.handleOpen(e)
    }

    /**
     * 处理关闭事件
     */
    override fun handleClose(e: InventoryCloseEvent) {
        restorePlayerInventory()
        super.handleClose(e)
    }

    /**
     * 备份玩家背包的物品
     */
    private fun backupPlayerInventory() {
        inventorySet.getPlayerInventory().apply {
            playerItems = storageContents
            clear()
        }
    }

    /**
     * 恢复玩家背包的物品
     */
    private fun restorePlayerInventory() {
        inventorySet.getPlayerInventory().apply { storageContents = playerItems }
    }

}