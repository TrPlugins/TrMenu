package cc.trixey.mc.trmenu.legacy.invero.event

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/10/21
 */
class InveroCloseEvent(
    player: Player,
    invero: cc.trixey.mc.trmenu.legacy.invero.Invero,
) : InveroEvent(player, invero) {

    override val allowCancelled = false

    var shouldUpdate = true

}