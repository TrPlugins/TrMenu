package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.internal.command.CommandExpresser
import net.md_5.bungee.api.ChatColor.COLOR_CHAR
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

/**
 * @author Arasple
 * @date 2021/1/28 20:11
 */
object CommandList : CommandExpresser {

    // menu list [filter]
    override val command = subCommand {
        execute<CommandSender> { sender, context, argument ->
            find(sender, null)
        }
        dynamic(optional = true) {
            execute<CommandSender> { sender, context, argument ->
                find(sender, argument)
            }
        }
    }

    private fun find(sender: CommandSender, filter: String?) {
        val menus = Menu.menus.filter { filter == null || it.id.contains(filter, true) }.sortedBy { it.id }

        if (menus.isEmpty()) {
            sender.sendLang("Command-List-Error", filter ?: "*")
        } else {
            sender.sendLang("Command-List-Header", menus.size, filter ?: "*")
            menus.forEach {
                sender.sendLang(
                    "Command-List-Format", it.id,
                    COLOR_CHAR + it.type.color.char.toString() + it.type.name,
                    it.settings.title.elements.first(),
                    it.icons.size
                )
            }
        }
    }

}