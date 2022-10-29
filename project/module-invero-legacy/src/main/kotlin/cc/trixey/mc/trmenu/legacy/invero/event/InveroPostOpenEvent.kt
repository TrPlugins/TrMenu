package cc.trixey.mc.trmenu.legacy.invero.event

import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/10/22
 */
class InveroPostOpenEvent(
    player: Player,
    invero: cc.trixey.mc.trmenu.legacy.invero.Invero,
    val previous: cc.trixey.mc.trmenu.legacy.invero.Invero? = null
) : InveroEvent(player, invero)