package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.impl.container.InveroManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/11/12 20:54
 */
object UnitPrinter {

    val print = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendMessage("Size: ${InveroManager.getWindows().size}")
            InveroManager.getWindows().forEach {
                sender.sendMessage(":Window: ${it.type}")
            }
        }
    }

}