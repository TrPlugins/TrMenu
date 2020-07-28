package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.display.Menu
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/3/23 17:23
 */
class CommandListMenu : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Filter")
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val filter = if (args.isNotEmpty()) args.joinToString(" ") else null
        val menus = Menu.getMenus().filter {
            filter == null || it.id.contains(filter, true)
        }

        TLocale.sendTo(sender, "COMMANDS.LIST.HEADER", menus.size, filter ?: "*")
        menus.forEach {
            TLocale.sendTo(sender, "COMMANDS.LIST.FORMAT", it.id)
        }
    }

}