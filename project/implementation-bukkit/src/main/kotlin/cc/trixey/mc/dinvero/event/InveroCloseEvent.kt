package cc.trixey.mc.dinvero.event

import cc.trixey.mc.dinvero.Invero
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/10/21
 */
class InveroCloseEvent(
    player: Player,
    invero: Invero,
) : InveroEvent(player, invero) {

    override val allowCancelled = false

    var shouldUpdate = true

}