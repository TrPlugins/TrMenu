package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
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
class PairedInventory(var container: Inventory, private val playerUUID: UUID?) : InventorySet {

    constructor(window: Window, player: Player?) : this(
        window.let {
            if (it.type.isOrdinaryChest) {
                Bukkit.createInventory(WindowHolder(it), it.type.containerSize, it.title)
            } else {
                Bukkit.createInventory(WindowHolder(it), it.type.bukkitType, it.title)
            }
        }, player?.uniqueId
    )

    val isComplete by lazy {
        playerUUID != null
    }

    override fun getContainerSize(): Int {
        return container.size
    }

    override fun getPlayerInventory(): PlayerInventory {
        return getPlayerInventorySafely() ?: throw NullPointerException("getPlayerInventory() is null")
    }

    override fun getPlayerInventorySafely(): PlayerInventory? {
        return playerUUID?.let { getProxyPlayer(it)?.castSafely<Player>()?.inventory }
    }

    override fun clear() {
        container.clear()
        getPlayerInventorySafely()?.clear()
    }

    override operator fun get(slot: Int): ItemStack? {
        return if (slot + 1 > container.size) {
            getPlayerInventorySafely()?.getItem(slot.outflowCorrect() - container.size)
        } else {
            container.getItem(slot)
        }
    }

    override operator fun set(slot: Int, itemStack: ItemStack?) {
        if (slot + 1 > container.size) {
            getPlayerInventorySafely()?.setItem(slot.outflowCorrect(), itemStack)
        } else {
            container.setItem(slot, itemStack)
        }
    }

}