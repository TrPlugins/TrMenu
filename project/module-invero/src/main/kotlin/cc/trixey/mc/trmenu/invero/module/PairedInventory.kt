package cc.trixey.mc.trmenu.invero.module

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.PlayerInventory
import taboolib.common.platform.function.getProxyPlayer
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 17:04
 */
class PairedInventory(var container: Inventory, private val playerUUID: UUID?) {

    constructor(container: Inventory, player: Player) : this(container, player.uniqueId)

    val isComplete by lazy {
        playerUUID != null
    }

    fun getPlayerInventory(): PlayerInventory {
        return getPlayerInventorySafely() ?: throw NullPointerException("getPlayerInventory() is null")
    }

    fun getPlayerInventorySafely(): PlayerInventory? {
        return playerUUID?.let { getProxyPlayer(it)?.castSafely<Player>()?.inventory }
    }

}