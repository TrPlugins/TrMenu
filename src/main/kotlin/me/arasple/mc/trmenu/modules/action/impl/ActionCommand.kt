package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.util.Commands
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:16
 */
class ActionCommand : Action("command|cmd|player|execute") {

    override fun onExecute(player: Player) = getContentSplited(player, ";").forEach {
        Commands.dispatchCommand(player, it)
    }

}