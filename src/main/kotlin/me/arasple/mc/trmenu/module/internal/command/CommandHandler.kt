package me.arasple.mc.trmenu.module.internal.command

import io.izzel.taboolib.module.command.base.*
import me.arasple.mc.trmenu.module.internal.command.impl.*


/**
 * @author Arasple
 * @date 2020/5/30 14:11
 */
@BaseCommand(name = "trmenu", aliases = ["menu"], permission = "trmenu.access")
class CommandHandler : BaseMainCommand() {

    @SubCommand(permission = "test", description = "Test loaded menus")
    val test: BaseSubCommand = CommandTest()

    @SubCommand(permission = "trmenu.command.list", description = "List loaded menus")
    val list: BaseSubCommand = CommandList()

    @SubCommand(permission = "trmenu.command.open", description = "Open a menu for player")
    val open: BaseSubCommand = CommandOpen()

    @SubCommand(permission = "trmenu.command.reload", description = "Reload all menus")
    val reload: BaseSubCommand = CommandReload()

    @SubCommand(permission = "trmenu.command.template", description = "Quick layout menu", type = CommandType.PLAYER)
    val template: BaseSubCommand = CommandTemplate()

    @SubCommand(permission = "trmenu.command.action", description = "Run actions for test")
    val action: BaseSubCommand = CommandAction()

    @SubCommand(permission = "trmenu.command.item", description = "Manipulate items")
    val item: BaseSubCommand = CommandItem()

    @SubCommand(permission = "trmenu.command.sounds", description = "Preview & test sounds", type = CommandType.PLAYER)
    var sounds: BaseSubCommand = CommandSounds()

    @SubCommand(permission = "trmenu.command.debug", description = "Print debug info")
    val debug: BaseSubCommand = CommandDebug()

}