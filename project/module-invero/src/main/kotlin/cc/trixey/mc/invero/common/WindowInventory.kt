package cc.trixey.mc.invero.common

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import taboolib.common.platform.function.getProxyPlayer
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 17:04
 */
class WindowInventory(var container: Inventory, private val playerUUID: UUID?) : InventorySet {

    constructor(window: Window, player: Player?) : this(
        window.let {
            if (it.type.isOrdinaryChest) {
                Bukkit.createInventory(WindowHolder(it), it.type.containerSize, it.title)
            } else {
                Bukkit.createInventory(WindowHolder(it), it.type.bukkitType, it.title)
            }
        }, player?.uniqueId
    )

    /**
     * @see InventorySet.isCompleteSet
     */
    override val isCompleteSet: Boolean
        get() = playerUUID != null

    /**
     * @see InventorySet.open
     */
    override fun open(player: Player) {
        player.openInventory(container)
    }

    /**
     * @see InventorySet.getContainerSize
     */
    override fun getContainerSize(): Int {
        return container.size
    }

    /**
     * @see InventorySet.getPlayerInventory
     */
    override fun getPlayerInventory(): PlayerInventory {
        return getPlayerInventorySafely() ?: error("getPlayerInventory() is null")
    }

    /**
     * @see InventorySet.getPlayerInventorySafely
     */
    override fun getPlayerInventorySafely(): PlayerInventory? {
        return playerUUID?.let { getProxyPlayer(it)?.castSafely<Player>()?.inventory }
    }

    /**
     * @see InventorySet.clear
     */
    override fun clear() {
        container.clear()
        getPlayerInventorySafely()?.clear()
    }

    /**
     * @see InventorySet.get
     */
    override operator fun get(slot: Int): ItemStack? {
        return if (slot + 1 > container.size) {
            getPlayerInventorySafely()?.getItem(slot.outflowCorrect() - container.size)
        } else {
            container.getItem(slot)
        }
    }

    /**
     * @see InventorySet.set
     */
    override operator fun set(slot: Int, itemStack: ItemStack?) {
        if (slot + 1 > container.size) {
            getPlayerInventorySafely()?.setItem(slot.outflowCorrect(), itemStack)
        } else {
            container.setItem(slot, itemStack)
        }
    }

}