package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/11/12 20:54
 */
object UnitPrinter {

    val print = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendMessage("Size: ${WindowHolder.runningWindows.size}")
            WindowHolder.runningWindows.forEach {
                sender.sendMessage(":Window: ${it.type}")
            }
        }
    }

}