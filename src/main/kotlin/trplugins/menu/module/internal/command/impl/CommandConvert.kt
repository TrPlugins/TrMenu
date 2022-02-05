package trplugins.menu.module.internal.command.impl

import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.platform.util.sendLang
import trplugins.menu.api.suffixes
import trplugins.menu.module.display.Menu
import trplugins.menu.module.internal.command.CommandExpression
import java.io.File

/**
 * TrMenu
 * trplugins.menu.module.internal.command.impl.CommandConvert
 *
 * @author Score2
 * @since 2022/02/05 17:02
 */
object CommandConvert : CommandExpression {

    // trm convert [Menu] [Type]
    override val command = subCommand {
        // menu
        dynamic {
            suggestion<CommandSender> { sender, context ->
                Menu.menus.map { it.id }
            }
            // type
            dynamic {
                suggestion<CommandSender>(uncheck = true) { sender, context ->
                    Type.values().map { it.name }
                }

                execute<CommandSender> { sender, context, argument ->
                    val menu = Menu.menus.find { it.id == context.argument(-1) }
                        ?: return@execute sender.sendLang("Command-Convert-Unknown-Menu", context.argument(-1))
                    val type = Type.values().find { it.name == argument || it.suffixes.contains(argument) }
                        ?: return@execute sender.sendLang("Command-Convert-Unknown-Type", argument)

                    if (menu.conf.type == type)
                        return@execute sender.sendLang("Command-Convert-Type-Already", menu.id, type.name)

                    val converted = Configuration.empty(type).also { it.root = menu.conf.root }
                    menu.conf.file?.let {
                        // 备份原文件
                        File(it.parentFile, "${it.name}.bak").let {
                            it.renameTo(it)
                        }
                        // 释放转换产物
                        File(it.parentFile, "${it.nameWithoutExtension}.${type.suffixes[0]}").let {
                            converted.file = it
                            converted.saveToFile()
                        }
                    }
                    // 替换配置文件
                    menu.conf = converted
                    sender.sendLang("Command-Convert-Converted", menu.id, type.name)
                }
            }
        }
    }
}