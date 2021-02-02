package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.module.command.base.BaseSubCommand
import me.arasple.mc.trmenu.module.conf.Loader
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2021/1/27 11:44
 */
class CommandReload : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        Loader.loadMenus(sender)
    }

}