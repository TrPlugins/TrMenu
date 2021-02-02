package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.module.display.Menu
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2021/1/28 20:11
 */
class CommandList : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Filter", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val filter = if (args.isNotEmpty()) args.joinToString(" ") else null
        val menus = Menu.menus.filter { filter == null || it.id.contains(filter, true) }.sortedBy { it.id }

        if (menus.isEmpty()) {
            TLocale.sendTo(sender, "Command.List.Error", filter ?: "*")
        } else {
            TLocale.sendTo(sender, "Command.List.Header", menus.size, filter ?: "*")

            menus.forEach {
                TLocale.sendTo(
                    sender, "Command.List.Format", it.id,
                    it.settings.title.elements.first(),
                    it.icons.size
                )
            }
        }
    }

}