package cc.trixey.mc.dinvero.event

import cc.trixey.mc.dinvero.Invero
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/10/22
 */
class InveroPostOpenEvent(player: Player, invero: Invero, val previous: Invero? = null) : InveroEvent(player, invero)