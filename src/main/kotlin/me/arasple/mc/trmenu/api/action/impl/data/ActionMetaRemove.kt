package me.arasple.mc.trmenu.api.action.impl.data

import me.arasple.mc.trmenu.api.Extends.removeMeta
import me.arasple.mc.trmenu.api.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/18 22:10
 */
class ActionMetaRemove : Action("(remove|rem|del)(-)?(temp|var(iable)?|meta)(s)?") {

    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach {
        when {
            it.startsWith("^") -> {
                val match = it.removePrefix("^")
                player.removeMeta { key -> key.startsWith(match) }
            }
            it.startsWith("$") -> {
                val match = it.removePrefix("$")
                player.removeMeta { key -> key.endsWith(match) }
            }
            else -> {
                player.removeMeta("{meta:$it}")
            }
        }
    }

}