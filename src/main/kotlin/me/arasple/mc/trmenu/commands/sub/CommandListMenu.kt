package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.display.Menu
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/3/23 17:23
 */
class CommandListMenu : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Filter", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val filter = if (args.isNotEmpty()) args.joinToString(" ") else null
        val menus = Menu.getMenus().filter { filter == null || it.id.contains(filter, true) }.sortedBy { it.id }

        sender.sendLocale("COMMANDS.LIST.HEADER", menus.size, filter ?: "*")
        menus.forEach { sender.sendLocale("COMMANDS.LIST.FORMAT", it.id) }
    }

}