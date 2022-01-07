package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.TrMenu.SETTINGS
import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.internal.command.CommandExpression
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