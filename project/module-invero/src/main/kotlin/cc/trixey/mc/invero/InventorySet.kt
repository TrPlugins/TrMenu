package cc.trixey.mc.invero

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

/**
 * @author Arasple
 * @since 2022/11/18 10:16
 */
interface InventorySet {

    /**
     * 是否为完整的一对容器
     *
     * 即是否包含玩家背包容器
     */
    val isCompleteSet: Boolean

    /**
     * 开启
     */
    fun open(player: Player)

    /**
     * 获取容器大小
     */
    fun getContainerSize(): Int

    /**
     * 取得玩家的容器背包
     */
    fun getPlayerInventory(): PlayerInventory

    /**
     * 取得玩家的容器背包（可返回空地）
     */
    fun getPlayerInventorySafely(): PlayerInventory?

    /**
     * 清除所有容器
     */
    fun clear()

    /**
     * 取得槽位物品
     */
    operator fun get(slot: Int): ItemStack?

    /**
     * 设置槽位物品
     */
    operator fun set(slot: Int, itemStack: ItemStack?)

    /**
     * 拓展函数
     *
     * 修复溢出的槽位
     */
    fun Int.outflowCorrect() = (this - getContainerSize()).let {
        if (it > 26) it - 27 else it + 9
    }

}