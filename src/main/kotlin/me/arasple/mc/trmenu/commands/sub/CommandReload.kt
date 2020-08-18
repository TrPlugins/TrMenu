package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.BaseSubCommand
import me.arasple.mc.trmenu.modules.configuration.MenuLoader
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/3/1 19:36
 */
class CommandReload : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        MenuLoader.loadMenus(sender)
    }

}