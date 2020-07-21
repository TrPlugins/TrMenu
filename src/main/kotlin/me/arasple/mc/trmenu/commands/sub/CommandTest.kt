package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/21 10:30
 */
class CommandTest : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val player = sender as Player
    }

    override fun getType(): CommandType = CommandType.PLAYER

}