package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.data.MetaPlayer.setArguments
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:33
 */
class ActionSetArgs : Action("set(-)?arg(ument)?(s)?") {

    override fun onExecute(player: Player) {
        val split = getContent().split(" ").toMutableList()
        split.indices.forEach {
            split[it] = Msger.replace(player, split[it])
        }
        player.setArguments(split.toTypedArray())
    }

}