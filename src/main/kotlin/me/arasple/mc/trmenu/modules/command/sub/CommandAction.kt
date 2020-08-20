package me.arasple.mc.trmenu.modules.command.sub

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.util.ArrayUtil
import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.api.action.Actions
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/7/22 10:52
 */
class CommandAction : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Player"),
        Argument("Action")
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val player = Bukkit.getPlayerExact(args[0])

        if (player == null || !player.isOnline) {
            sender.sendLocale("COMMANDS.ACTION.UNKNOWN-PLAYER")
            return
        }
        ArrayUtil.arrayJoin(args, 1).let { it ->
            val print = it.startsWith("#")
            val action = Actions.cachedAction(it.removePrefix("#"))

            Actions.runActions(player, action)
            if (print) {
                action.forEach { sender.sendMessage("${ChatColor.COLOR_CHAR}7${it.toDetailedString()}") }
            }
        }
    }

}