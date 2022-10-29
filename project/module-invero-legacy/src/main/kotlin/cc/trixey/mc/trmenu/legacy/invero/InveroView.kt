package cc.trixey.mc.trmenu.legacy.invero

import cc.trixey.mc.trmenu.legacy.invero.nms.sendWindowClose
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.getProxyPlayer
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/21
 */
class InveroView(val invero: Invero, var viewer: UUID?) {

    val playerContents by lazy {
        PlayerContents(getViewer()!!)
    }

    class PlayerContents(val contents: Array<ItemStack?>) {

        constructor(player: Player) : this(read(player))

        companion object {

            private fun read(player: Player): Array<ItemStack?> {
                val hotbar = mutableListOf<ItemStack?>()
                val top = mutableListOf<ItemStack?>()
                player.inventory.contents.forEachIndexed { index, itemStack ->
                    if (index <= 8) hotbar.add(itemStack)
                    else top.add(itemStack)
                }
                return (top + hotbar).toTypedArray()
            }

        }
    }

    fun forViewer(function: (Player) -> Unit) {
        getViewer()?.let(function)
    }

    fun close(closePacket: Boolean = true) {
        if (closePacket) {
            getViewer()?.sendWindowClose(invero.pool.index)
            viewer = null
        }
    }

    fun hasViewer() = getViewer()?.isOnline ?: false

    fun getViewer(): Player? {
        return viewer?.let { getProxyPlayer(it)?.castSafely<Player>() }
    }

    fun isViewing(player: Player): Boolean {
        return viewer == player.uniqueId
    }

    fun setViewing(player: Player) {
        close()
        viewer = player.uniqueId
    }

}