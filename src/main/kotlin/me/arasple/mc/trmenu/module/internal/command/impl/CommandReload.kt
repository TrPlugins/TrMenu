package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.internal.command.CommandExpresser
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @date 2021/1/27 11:44
 */
object CommandReload : CommandExpresser {

    override val command = subCommand {
        execute<CommandSender> { sender, _, _ ->
            TrMenu.SETTINGS.reload()
            Loader.loadMenus(sender)
        }
    }
}