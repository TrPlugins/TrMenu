package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.invero.window.CompleteWindow
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/10/29 10:51
 */
@CommandHeader(name = "testInvero")
object TestInvero {

    @CommandBody
    val release = subCommand {
        execute<Player> { player, _, _ ->
            val window = CompleteWindow(player)

            window.title = "Hello Invero"
            window.open()
        }
    }

}