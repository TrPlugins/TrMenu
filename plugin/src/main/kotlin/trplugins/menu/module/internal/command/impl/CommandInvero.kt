package trplugins.menu.module.internal.command.impl

import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand
import trplugins.menu.module.display.Menu
import trplugins.menu.module.internal.command.CommandExpression

/**
 * TrMenu
 * trplugins.menu.module.internal.command.impl.CommandInvero
 *
 * @author Score2
 * @since 2023/01/18 22:03
 */
object CommandInvero : CommandExpression {

    override val command = subCommand {
        execute<ConsoleCommandSender> { sender, context, argument ->
            if (!Bukkit.getPluginManager().isPluginEnabled("Invero")) {
                return@execute sender.sendMessage("§c[TrMenu -> Invero] §7Invero is uninstalled.")
            }

            val menusV4 = Menu.menus.map { menuV3 ->




            }

        }
    }

}