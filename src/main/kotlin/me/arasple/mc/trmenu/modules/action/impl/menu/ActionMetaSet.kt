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
        getContentSplited(player, ";").forEach {
            val split = it.split(" ", limit = 2)
            if (split.size == 2) {
                player.setMeta("{meta:${split[0]}}", split[1])
                Msger.debug("ACTIONS.SET-TEMP-VARIABLE", player.name, split[0], split[1], it)
            } else {
                Msger.debug("ACTIONS.SET-TEMP-VARIABLE-FAILED", player.name, it)
            }
        }

}