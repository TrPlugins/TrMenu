package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender


/**
 * @author Arasple
 * @date 2020/7/22 12:08
 */
class CommandTemplate : BaseSubCommand() {

    override fun getArguments(): Array<Argument> =
        arrayOf(
            Argument("Rows", false) {
                listOf("1", "2", "3", "4", "5", "6")
            }
        )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        sender.sendMessage("模板你妈呢 还没写")
    }

}