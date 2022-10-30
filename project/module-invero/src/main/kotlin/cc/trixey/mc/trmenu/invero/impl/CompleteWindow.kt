package cc.trixey.mc.trmenu.invero.impl

import cc.trixey.mc.trmenu.invero.module.PairedInventory
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/10/29 16:44
 */
class CompleteWindow(
    viewer: Player,
) : ContainerWindow(viewer) {

    /**
     * 查看此类型 Window 自动备份玩家背包的实际物品
     * 此备份保留了原有物品的准确位置
     *
     * 实际 Window 调用中，是可以影响物品但不会
     */
    val playerItems = arrayOfNulls<ItemStack>(36)

    /**
     * PairedInventory 对象
     *
     * 可调用 Window 的容器 Inventory，
     * 以及玩家的 PlayerInventory 对象
     */
    override val pairedInventory: PairedInventory by lazy {
        PairedInventory(Bukkit.createInventory(WindowHolder(this), type.bukkitType, title), viewer)
    }

    private fun backupPlayerInventory() {
        pairedInventory.getPlayerInventory().apply {
            contents.forEachIndexed { index, itemStack ->
                playerItems[index] = itemStack
            }
            clear()
        }
    }

    private fun restorePlayerInventory() {
        pairedInventory.getPlayerInventory().apply {
            playerItems.forEachIndexed { index, itemStack ->
                contents[index] = itemStack
            }
        }
    }

}