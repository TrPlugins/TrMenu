package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/28 20:11
 */
class CommandOpen : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("MenuId") { Menu.menus.map { it.id } },
        Argument("Player", false),
        Argument("Arguments", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val split = args[0].split(":")
        val menu = TrMenuAPI.getMenuById(split[0])
        val page = split.getOrNull(1)?.toIntOrNull() ?: 0
        val player = if (args.size > 1) Bukkit.getPlayerExact(args[1]) else if (sender is Player) sender else null
        val arguments = if (args.size > 2) ArrayUtils.removeAll(args, 0, 1) else null

        if (menu == null) {
            TLocale.sendTo(sender, "Command.Open.Unknown-Menu", args[0])
            return
        }
        if (player == null || !player.isOnline) {
            TLocale.sendTo(sender, "Command.Open.Unknown-Player", args.getOrNull(1))
            return
        }

        menu.open(player, page, MenuOpenEvent.Reason.PLAYER_COMMAND) {
            if (!Metadata.byBukkit(player, "FORCE_ARGS") || (arguments != null && arguments.isNotEmpty())) {
                it.arguments = arguments ?: arrayOf()
            }
        }
    }

}