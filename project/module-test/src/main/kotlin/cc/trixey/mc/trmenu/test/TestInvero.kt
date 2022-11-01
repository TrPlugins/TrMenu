package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.window.ContainerWindow
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync

/**
 * @author Arasple
 * @since 2022/10/29 10:51
 */
@CommandHeader(name = "testInvero")
object TestInvero {

    @CommandBody
    val release = subCommand {
        execute<Player> { player, _, _ ->
            val window = ContainerWindow(player)

            window.title = "Hello Invero"
            window.open()

            submit(delay = 20L) {
                player.sendMessage("Animated Title Test Submited.")
                window.testAnimatedTitle()
            }
        }
    }

    /**
     * 测试动态标题的播放性能
     */
    private fun Window.testAnimatedTitle(frameTicks: Long = 1) {
        var current = ""
        val titles = mutableListOf<String>()
        var index = 0

        "Invero Animated Title".windowed(1, 1).forEachIndexed { _, s ->
            current += s
            titles.add(current)
        }
        titles.addAll(titles.reversed())

        submitAsync(period = frameTicks) {
            if (!hasViewer()) cancel().also { return@submitAsync }
            title = titles[index++]
            if (index == titles.size - 1) index = 0
        }
    }

}