package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/27 9:02
 */
class ActionTell : Action("tell|send|message|msg|talk|say") {

    override fun onExecute(player: Player) = getContentSplited(player).forEach { player.sendMessage(it) }

}