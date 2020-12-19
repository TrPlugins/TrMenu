package me.arasple.mc.trmenu.modules.command.sub

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.api.Extends.setArguments
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.modules.display.Menu
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/3 14:25
 */
class CommandOpen : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("MenuId") { Menu.getMenus().map { it.id } },
        Argument("Player", false),
        Argument("Arguments", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val split = args[0].split(":")
        val menu = TrMenuAPI.getMenuById(split[0])
        val page = if (split.size > 1) NumberUtils.toInt(split[1], -1) else -1
        val player = if (args.size > 1) Bukkit.getPlayerExact(args[1]) else if (sender is Player) sender else null
        val arguments = if (args.size > 2) ArrayUtils.removeAll(args, 0, 1) else null

        if (menu == null) {
            sender.sendLocale("COMMANDS.OPEN.UNKNOWN-MENU", args[0])
            return
        }
        if (player == null) {
            sender.sendLocale("COMMANDS.OPEN.UNKNOWN-PLAYER")
            return
        }
        player.setArguments(arguments)
        menu.open(
            player,
            page,
            if (sender is Player) MenuOpenEvent.Reason.PLAYER_COMMAND else MenuOpenEvent.Reason.CONSOLE
        )
    }

}