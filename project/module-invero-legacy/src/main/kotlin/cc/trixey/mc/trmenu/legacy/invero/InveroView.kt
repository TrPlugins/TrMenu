package cc.trixey.mc.trmenu.legacy.invero

import cc.trixey.mc.trmenu.legacy.invero.nms.sendWindowClose
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.onlinePlayers

/**
 * @author Arasple
 * @since 2022/10/21
 */
class InveroView(val invero: Invero) {

    val viewers = LinkedHashMap<String, PlayerContents>()

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

    fun getPlayerContents(player: Player): PlayerContents {
        val contents = viewers[player.name]
        assert(contents != null) { "Player ${player.name} is has no viewing contents" }

        return contents!!
    }

    fun forViewers(function: (Player) -> Unit) {
        onlinePlayers.filter(::isViewing).forEach(function)
    }

    fun closeAll(closePacket: Boolean = true) {
        forViewers {
            close(it, closePacket)
        }
    }

    fun close(player: Player, closePacket: Boolean = true) {
        removeViewing(player)
        if (closePacket) {
            player.sendWindowClose(invero.pool.index)
        }
    }

    fun setViewing(player: Player) {
        assert(!viewers.containsKey(player.name)) { "Player ${player.name} is alreday viewing this invero" }

        viewers[player.name] = PlayerContents(player)
    }

    fun removeViewing(player: Player): Boolean {
        return viewers.remove(player.name) != null
    }

    fun isViewing(player: Player): Boolean {
        return player.name in viewers.keys
    }

    fun hasViewer(): Boolean {
        return viewers.isNotEmpty()
    }

}