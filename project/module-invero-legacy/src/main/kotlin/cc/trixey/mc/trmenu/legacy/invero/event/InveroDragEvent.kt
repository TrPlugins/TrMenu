package cc.trixey.mc.trmenu.legacy.invero.event

import cc.trixey.mc.trmenu.legacy.invero.Invero
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/10/29 19:45
 */
class InveroDragEvent(player: Player, invero: Invero, val type: DragType, val slots: List<Int>) :
    InveroEvent(player, invero) {

    enum class DragType {

        SINGLE,

        EVEN

    }

}