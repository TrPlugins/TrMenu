package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.module.internal.command.CommandExpresser
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang
import kotlin.system.measureNanoTime

/**
 * @author Arasple
 * @date 2021/1/31 10:41
 */
object CommandAction : CommandExpresser {

    // menu action [Player] [Action]
    override val command = subCommand {
        // player
        dynamic {
            suggestion<CommandSender>(uncheck = true) { sender, context ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            // Action
            dynamic {
                execute<CommandSender> { sender, context, argument ->
                    val player = context.argument(-1).let { Bukkit.getPlayerExact(it) }
                    if (player == null || !player.isOnline) {
                        sender.sendLang("Command-Action-Unknown-Player", context.argument(-1))
                        return@execute
                    }

                    val (hidePrint, action) = argument.startsWith("#") to Actions.readAction(argument.removePrefix("#"))

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

}