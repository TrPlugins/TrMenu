package cc.trixey.mc.trmenu.invero.impl

import cc.trixey.mc.trmenu.invero.module.PairedInventory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/10/29 16:44
 *
 * 一个完整的容器窗口
 * 玩家的背包默认会被缓存 & 清空，关闭时予以恢复
 */
class CompleteWindow(viewer: Player) : ContainerWindow(viewer) {

    /**
     * 查看此类型 Window 自动备份玩家背包的实际物品
     * 此备份保留了原有物品的准确位置
     */
    val playerItems = arrayOfNulls<ItemStack>(36)

    /**
     * PairedInventory 对象
     *
     * 可调用 Window 的容器 Inventory，
     * 以及玩家的 PlayerInventory 对象
     */
    override val pairedInventory: PairedInventory by lazy {
        PairedInventory(this, viewer)
    }

    override fun handleOpen(e: InventoryOpenEvent) {
        backupPlayerInventory()
        super.handleOpen(e)
    }

    override fun handleClose(e: InventoryCloseEvent) {
        restorePlayerInventory()
        super.handleClose(e)
    }

    /**
     * 缓存玩家的背包物品
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
     * 恢复玩家背包的物品
     */
    private fun restorePlayerInventory() {
        pairedInventory.getPlayerInventory().apply {
            playerItems.forEachIndexed { index, itemStack ->
                storageContents[index] = itemStack
            }
        }
    }

}