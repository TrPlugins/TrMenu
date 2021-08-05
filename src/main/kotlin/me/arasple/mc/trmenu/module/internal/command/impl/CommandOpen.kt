package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.internal.command.CommandExpresser
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.subCommand
import taboolib.platform.util.sendLang

/**
 * @author Arasple
 * @date 2021/1/28 20:11
 */
object CommandOpen : CommandExpresser {

    override val description = "Open a menu for player"

    // menu open [menuId] [player] [args...]
    override val command = subCommand {
        // menuId
        dynamic {
            suggestion<CommandSender> { _, _ ->
                Menu.menus.map { it.id }
            }

            execute<CommandSender> { sender, context, argument ->
                val split = context.argument(0)!!.split(":")
                val menu = TrMenuAPI.getMenuById(split[0])
                val page = split.getOrNull(1)?.toIntOrNull() ?: 0
                val player = if (sender is Player) sender else null
                if (menu == null) {
                    sender.sendLang("Command-Open-Unknown-Menu", argument)
                    return@execute
                }
                if (player == null || !player.isOnline) {
                    sender.sendLang("Command-Open-Unknown-Player", "CONSOLE")
                    return@execute
                }
                menu.open(player, page, MenuOpenEvent.Reason.PLAYER_COMMAND) {
                    arrayOf<String>()
                }

            }

            // player
            dynamic(optional = true) {
                suggestion<CommandSender> { _, _ ->
                    Bukkit.getOnlinePlayers().map { it.name }
                }

                execute<CommandSender> { sender, context, argument ->
                    val split = context.argument(-1)!!.split(":")
                    val menu = TrMenuAPI.getMenuById(split[0])
                    val page = split.getOrNull(1)?.toIntOrNull() ?: 0
                    val player = context.argument(0).let { if (it == null) null else Bukkit.getPlayerExact(it) }
                    val arguments = argument.substringAfter(" ").let { if (it.contains(" ")) it.split(" ").toTypedArray() else null }

                    if (menu == null) {
                        sender.sendLang("Command-Open-Unknown-Menu", context.argument(-1)!!)
                        return@execute
                    }
                    if (player == null || !player.isOnline) {
                        sender.sendLang("Command-Open-Unknown-Player", context.argument(0) ?: "null")
                        return@execute
                    }

                    menu.open(player, page, MenuOpenEvent.Reason.PLAYER_COMMAND) {
                        if (!Metadata.byBukkit(player, "FORCE_ARGS") || (arguments != null && arguments.isNotEmpty())) {
                            it.arguments = arguments ?: arrayOf()
                        }
                    }
                }
            }
        }
    }

}