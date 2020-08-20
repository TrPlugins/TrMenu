package me.arasple.mc.trmenu.api.action.impl.menu

import me.arasple.mc.trmenu.api.Extends.setArguments
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.util.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:33
 */
class ActionSetArgs : Action("set(-)?arg(ument)?(s)?") {

    override fun onExecute(player: Player) {
        player.setArguments(
            getContentSplited(player, " ")
                .map { Msger.replace(player, replaceWithSpaces(it)) }
                .toTypedArray()
        )
    }

}