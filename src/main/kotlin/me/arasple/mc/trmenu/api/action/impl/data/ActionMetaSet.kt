package me.arasple.mc.trmenu.api.action.impl.data

import me.arasple.mc.trmenu.api.Extends.setMeta
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.util.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/18 22:10
 */
class ActionMetaSet : Action("set(-)?(temp|var(iable)?|meta)(s)?") {

    override fun onExecute(player: Player) =
        getSplitedBySemicolon(player).forEach {
            val split = it.split(" ", limit = 2)
            if (split.size == 2) {
                val key = split[0]
                val value = replaceWithSpaces(split[1])

                player.setMeta("{meta:$key}", value)
                Msger.debug("ACTIONS.SET-META", player.name, key, value, it)
            } else {
                Msger.debug("ACTIONS.SET-META-FAILED", player.name, it)
            }
        }

}