package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.apache.commons.lang3.ArrayUtils
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

    override val command = subCommand {
        // menu open [menuId] [player] [args...]
        dynamic(optional = true) {
            suggestion<CommandSender> { _, _ ->
                Menu.menus.map { it.id }
            }
            dynamic(optional = true) {
                suggestion<CommandSender> { _, _ ->
                    Bukkit.getOnlinePlayers().map { it.name }
                }
            }
        }
        execute<CommandSender> { sender, context, _ ->
            val split = context.args[0].split(":")
            val menu = TrMenuAPI.getMenuById(split[0])
            val page = split.getOrNull(1)?.toIntOrNull() ?: 0
            val player = if (context.args.size > 1) Bukkit.getPlayerExact(context.args[1]) else if (sender is Player) sender else null
            val arguments = if (context.args.size > 2) ArrayUtils.removeAll(context.args, 0, 1) else null

            if (menu == null) {
                sender.sendLang("Command.Open.Unknown-Menu", context.args[0])
                return@execute
            }
            if (player == null || !player.isOnline) {
                sender.sendLang("Command.Open.Unknown-Player", context.args.getOrNull(1) ?: "null")
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