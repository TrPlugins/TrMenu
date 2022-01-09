package trplugins.menu.module.internal.command.impl

import trplugins.menu.TrMenu.SETTINGS
import trplugins.menu.module.conf.Loader
import trplugins.menu.module.internal.command.CommandExpression
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @date 2021/1/27 11:44
 */
object CommandReload : CommandExpression {

    override val command = subCommand {
        execute<CommandSender> { sender, _, _ ->
            SETTINGS.reload()
            Loader.loadMenus(sender, async = SETTINGS.getBoolean("Options.Async-Load-Menus", true))
        }
    }
}