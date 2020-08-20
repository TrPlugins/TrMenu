package me.arasple.mc.trmenu.api.action.impl

import io.izzel.taboolib.util.Commands
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:16
 */
class ActionCommand : Action("command|cmd|player|execute") {

    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach {
        Tasks.task {
            Commands.dispatchCommand(player, it)
        }
    }

}