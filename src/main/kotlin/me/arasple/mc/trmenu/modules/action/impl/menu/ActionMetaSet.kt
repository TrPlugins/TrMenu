package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.data.MetaPlayer.setMeta
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Msger
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
                player.setMeta("{meta:${split[0]}}", split[1])
                Msger.debug("ACTIONS.SET-META", player.name, split[0], split[1], it)
            } else {
                Msger.debug("ACTIONS.SET-META-FAILED", player.name, it)
            }
        }

}