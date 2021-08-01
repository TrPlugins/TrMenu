package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.kotlin.Indexed
import me.arasple.mc.trmenu.api.action.Actions
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.subCommand
import taboolib.platform.util.sendLang
import kotlin.system.measureNanoTime

/**
 * @author Arasple
 * @date 2021/1/31 10:41
 */
object CommandAction : CommandExpresser {

    override val command = subCommand {
        dynamic(optional = true) {
            suggestion<CommandSender> { sender, context ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
        }
        execute<CommandSender> { sender, context, argument ->
            val player = Bukkit.getPlayerExact(context.args[0])

            if (player == null || !player.isOnline) {
                sender.sendLang("Command-Action-Unknown-Player", context.args[0])
                return@execute
            }

            Indexed.join(context.args, 1).let { it ->
                val (hidePrint, action) = it.startsWith("#") to Actions.readAction(it.removePrefix("#"))

                measureNanoTime {
                    Actions.runAction(player, action).let {
                        sender.sendMessage("§8[§7Action§8] §7Result: §3$it")
                    }
                }.also {
                    if (!hidePrint) {
                        sender.sendMessage("§8[§7Action§8] §7Evaluated §8{$action} §7in §f${it.div(1000000.0)} ms")
                    }
                }
            }
        }
    }

}