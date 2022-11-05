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
class PairedInventory(var container: Inventory, private val playerUUID: UUID?) {

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

    fun getPlayerInventory(): PlayerInventory {
        return getPlayerInventorySafely() ?: throw NullPointerException("getPlayerInventory() is null")
    }

    fun getPlayerInventorySafely(): PlayerInventory? {
        return playerUUID?.let { getProxyPlayer(it)?.castSafely<Player>()?.inventory }
    }

    fun clear() {
        container.clear()
        getPlayerInventorySafely()?.clear()
    }

    operator fun get(absoluteSlot: Int): ItemStack? {
        return if (absoluteSlot + 1 >= container.size) {
            getPlayerInventorySafely()?.getItem(absoluteSlot - container.size)
        } else {
            container.getItem(absoluteSlot)
        }
    }

    operator fun set(absoluteSlot: Int, itemStack: ItemStack?) {
        if (absoluteSlot + 1 >= container.size) {
            getPlayerInventorySafely()?.setItem(absoluteSlot - container.size, itemStack)
        } else {
            container.setItem(absoluteSlot, itemStack)
        }
    }

}