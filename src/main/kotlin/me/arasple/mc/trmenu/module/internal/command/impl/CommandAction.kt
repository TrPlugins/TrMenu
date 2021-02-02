package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.kotlin.Indexed
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.api.action.Actions
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import kotlin.system.measureNanoTime

/**
 * @author Arasple
 * @date 2021/1/31 10:41
 */
class CommandAction : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Player", true),
        Argument("Action", true)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = Bukkit.getPlayerExact(args[0])

        if (player == null || !player.isOnline) {
            TLocale.sendTo(sender, "Command.Action.Unknown-Player", args[0])
            return
        }

        Indexed.join(args, 1).let { it ->
            val action = Actions.readAction(it.removePrefix("#"))

            measureNanoTime {
                Actions.runAction(player, action).let {
                    sender.sendMessage("§8[§7Action§8] §7Result: §3$it")
                }
            }.also { sender.sendMessage("§8[§7Action§8] §7Evaluated §8{$action} §7in §f${it.div(1000000.0)} ms") }

        }
    }

}