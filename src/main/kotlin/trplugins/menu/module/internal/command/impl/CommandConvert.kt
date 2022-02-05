package trplugins.menu.module.internal.command.impl

import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.platform.util.sendLang
import trplugins.menu.api.suffixes
import trplugins.menu.module.conf.Loader
import trplugins.menu.module.display.Menu
import trplugins.menu.module.internal.command.CommandExpression
import trplugins.menu.util.file.FileListener
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

                    // 进行转换
                    menu.conf.changeType(type)

                    menu.conf.file?.let { file: File ->
                        // 取消原文件监听器
                        FileListener.watcher.removeListener(file)
                        // 备份原文件
                        File(file.parentFile, "${file.name}.bak").let {
                            file.renameTo(it)
                        }
                        // 释放转换产物
                        File(file.parentFile, "${file.nameWithoutExtension}.${type.suffixes[0]}").let {
                            menu.conf.file = it
                            menu.conf.saveToFile()
                            // 开始监听新文件
                            Loader.listen(it)
                        }
                    }
                    sender.sendLang("Command-Convert-Converted", menu.id, type.name)
                }
            }
        }
    }
}