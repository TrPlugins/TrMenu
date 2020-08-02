package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Bungees
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:18
 */
class ActionConnect : Action("bungee|server|connect") {

    override fun onExecute(player: Player) = Bungees.connect(player, getContent(player))

}