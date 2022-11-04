package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.invero.impl.BasePanel
import cc.trixey.mc.trmenu.invero.impl.ContainerWindow
import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
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
            window.panels.add(BasePanel(window).apply {

                weight(PanelWeight.HIGH)

                item(ItemStack(Material.values().random()), (0..10).random())

            })

            window.open()

            submit(delay = 20L) {
                player.sendMessage("Test::Animated_Title::Submited")
                window.testAnimatedTitle()
            }
        }
    }

    @CommandBody
    val printDebug = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendMessage("Size: ${WindowHolder.runningWindows.size}")
            WindowHolder.runningWindows.forEach {
                sender.sendMessage(":Window: ${it.type}")
            }
        }
    }

    /**
     * 测试动态标题的播放性能
     */
    private fun Window.testAnimatedTitle(frameTicks: Long = 1) {
        var index = 0

        submitAsync(period = frameTicks) {
            if (!hasViewer()) cancel().also { return@submitAsync }
            title = animatedTitles[index++]
            if (index == animatedTitles.size - 1) index = 0
        }
    }

    private val animatedTitles by lazy {
        var current = ""
        val titles = mutableListOf<String>()

        "Invero Animated Title".windowed(1, 1).forEachIndexed { _, s ->
            current += s
            titles.add(current)
        }
        titles.addAll(titles.reversed())
        titles
    }

}