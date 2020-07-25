package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.data.MetaPlayer.setArguments
import me.arasple.mc.trmenu.display.Menu
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/3 14:25
 */
class CommandOpenMenu : BaseSubCommand() {

    override fun getArguments(): Array<Argument> = arrayOf(
        Argument("MenuId", true) { Menu.getMenus().map { it.id } },
        Argument("Player", false),
        Argument("Arguments", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val menu = TrMenuAPI.getMenuById(args[0])
        val player = if (args.size > 1) Bukkit.getPlayerExact(args[1]) else if (sender is Player) sender else null
        val arguments = if (args.size > 2) ArrayUtils.removeAll(args, 0, 1) else null

        if (menu == null) {
            TLocale.sendTo(sender, "COMMANDS.OPEN.UNKNOWN-MENU", args[0])
            return
        }
        if (player == null) {
            TLocale.sendTo(sender, "COMMANDS.OPEN.UNKNOWN-PLAYER")
            return
        }
        arguments?.let { player.setArguments(it) }
        menu.open(player, -1, if (sender is Player) MenuOpenEvent.Reason.PLAYER_COMMAND else MenuOpenEvent.Reason.CONSOLE)
    }

}