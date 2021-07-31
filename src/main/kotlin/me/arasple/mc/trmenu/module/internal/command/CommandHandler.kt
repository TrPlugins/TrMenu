package me.arasple.mc.trmenu.module.internal.command

import me.arasple.mc.trmenu.module.internal.command.impl.*
import taboolib.common.LifeCycle
import taboolib.common.platform.*


/**
 * @author Arasple
 * @date 2020/5/30 14:11
 */
@CommandHeader(name = "trmenu", aliases = ["menu"], permission = "trmenu.access")
object CommandHandler {

    /*
     * 暂时做了兼容, 命令框架应当需要大改.
     */
    @CommandBody(permission = "test"/*, description = "Test loaded menus"*/)
    val test = CommandTest

    @CommandBody(permission = "trmenu.command.list"/*, description = "List loaded menus"*/)
    val list = CommandList.command

    @CommandBody(permission = "trmenu.command.open"/*, description = "Open a menu for player"*/)
    val open = CommandOpen.command

    @CommandBody(permission = "trmenu.command.reload"/*, description = "Reload all menus"*/)
    val reload = CommandReload.command

    @CommandBody(permission = "trmenu.command.template"/*, description = "Quick layout menu", type = CommandType.PLAYER*/)
    val template = CommandTemplate.command

    @CommandBody(permission = "trmenu.command.action"/*, description = "Run actions for test"*/)
    val action = CommandAction.command

    @CommandBody(permission = "trmenu.command.item"/*, description = "Manipulate items"*/)
    val item = CommandItem.command

    @CommandBody(permission = "trmenu.command.sounds"/*, description = "Preview & test sounds", type = CommandType.PLAYER*/)
    var sounds = CommandSounds.command

    @CommandBody(permission = "trmenu.command.debug"/*, description = "Print debug info"*/)
    val debug = CommandDebug.command


    @Awake(LifeCycle.ENABLE)
    fun register() {
        command("trmenu", aliases = listOf("menu"), permission = "trmenu.access") {

        }
    }
}