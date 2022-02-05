package trplugins.menu.module.internal.command.impl

import trplugins.menu.module.display.Menu
import trplugins.menu.api.color
import trplugins.menu.module.internal.command.CommandExpression
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

/**
 * @author Arasple
 * @date 2021/1/28 20:11
 */
object CommandList : CommandExpression {

    // trm list [filter]
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
                    it.conf.type.color + it.conf.type.name,
                    it.settings.title.elements.first(),
                    it.icons.size
                )
            }
        }
    }

}