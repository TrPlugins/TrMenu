package me.arasple.mc.trmenu.modules.command.sub

import io.izzel.taboolib.util.Files
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.sendLocale
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.io.*

/**
 * @author Rubenicos
 * @date 2020/11/21 16:05
 */
class CommandDownload : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("FileName"),
        Argument("URL"),
        Argument("Path", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val folder = Files.folder(if (args.size > 2) args[2] else TrMenu.plugin.dataFolder.toString() + File.separator + "menus")
        val file = Files.file(folder.toString() + File.separator + if (args[0].contains(".")) args[0] else args[0] + ".yml")
        if (Files.downloadFile(args[1], file)) {
            sender.sendLocale("COMMANDS.DOWNLOAD.SUCCESS", args[0])
        } else {
            sender.sendLocale("COMMANDS.DOWNLOAD.FAILED")
        }
    }
}