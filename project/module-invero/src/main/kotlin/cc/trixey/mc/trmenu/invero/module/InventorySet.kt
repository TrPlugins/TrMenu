package cc.trixey.mc.trmenu.invero.module

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

/**
 * @author Arasple
 * @since 2022/11/18 10:16
 */
interface InventorySet {

    fun getContainerSize(): Int

    fun getPlayerInventory(): PlayerInventory

    fun getPlayerInventorySafely(): PlayerInventory?

    fun clear()

    operator fun get(slot: Int): ItemStack?

    operator fun set(slot: Int, itemStack: ItemStack?)

    fun Int.outflowCorrect() = (this - getContainerSize()).let {
        if (it > 26) it - 27 else it + 9
    }

}