package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.module.display.Menu
import org.bukkit.command.CommandSender
import taboolib.common.platform.subCommand
import taboolib.platform.util.sendLang

/**
 * @author Arasple
 * @date 2021/1/28 20:11
 */
object CommandList : CommandExpresser {

    // menu list [filter]
    override val command = subCommand {
        execute<CommandSender> { sender, context, argument ->
            val filter = context.argument(1).ifEmpty { null }
            val menus = Menu.menus.filter { filter == null || it.id.contains(filter, true) }.sortedBy { it.id }

            if (menus.isEmpty()) {
                sender.sendLang("Command-List-Error", filter ?: "*")
            } else {
                sender.sendLang("Command-List-Header", menus.size, filter ?: "*")

                menus.forEach {
                    sender.sendLang(
                        "Command-List-Format", it.id,
                        it.settings.title.elements.first(),
                        it.icons.size
                    )
                }
            }
        }
    }

}