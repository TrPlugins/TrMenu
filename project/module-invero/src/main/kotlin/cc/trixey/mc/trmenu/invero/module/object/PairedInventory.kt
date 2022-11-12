package cc.trixey.mc.trmenu.invero.module.`object`

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import cc.trixey.mc.trmenu.invero.module.Window
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
        return if (absoluteSlot + 1 > container.size) {
            getPlayerInventorySafely()?.getItem(absoluteSlot.outflowCorrect() - container.size)
        } else {
            container.getItem(absoluteSlot)
        }
    }

    operator fun set(absoluteSlot: Int, itemStack: ItemStack?) {
        if (absoluteSlot + 1 > container.size) {
            getPlayerInventorySafely()?.setItem(absoluteSlot.outflowCorrect(), itemStack)
        } else {
            container.setItem(absoluteSlot, itemStack)
        }
    }

    /**
     * Realistic:
     * 09,10,11,12,13,14,15,16,17
     * 18,19,20,21,22,23,24,25,26
     * 27,28,29,30,31,32,33,34,35
     *
     * 0, 1, 2, 3, 4, 5, 6, 7, 8
     *
     * Expected:
     * 01,02,03 ...
     * ...
     * ......     26,
     *
     * 27,28... 35
     */
    private fun Int.outflowCorrect(): Int {
        val substracted = this - container.size
        return if (substracted > 26) {
            substracted - 27
        } else {
            substracted + 9
        }
    }

}