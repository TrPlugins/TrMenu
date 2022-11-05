package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import cc.trixey.mc.trmenu.invero.impl.element.BaseItem
import cc.trixey.mc.trmenu.invero.impl.panel.BasePanel
import cc.trixey.mc.trmenu.invero.impl.window.ContainerWindow
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.util.buildElement
import cc.trixey.mc.trmenu.invero.util.buildPanel
import cc.trixey.mc.trmenu.invero.util.buildWindow
import org.bukkit.Material
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.buildItem

/**
 * @author Arasple
 * @since 2022/10/29 10:51
 *
 * TODO
 * [ ] Window 交互与 Panel、PanelElement 的传递
 * [ ] 嵌套 Panel 的实现和子父传递
 * [ ] 层叠 Panel 的交互、渲染处理
 * [ ] DynamicElement 的实现
 * [ ] Animation for Panel 动画组件的实现
 * [ ] Element 的主动请求更新
 * [ ] 更多常规类型的 Panel
 * [ ] 更多常规类型的 Element
 * [ ] 交互性、储存容器的实现
 */
@CommandHeader(name = "testInvero")
object TestInvero {

    @CommandBody
    val release = subCommand {
        execute { player, _, _ ->

            val window =
                buildWindow<ContainerWindow>(player) {

                    title = "Hello Invero"

                    buildPanel<BasePanel>(3 to 2) {
                        /*
                        Relative Layout:
                        012
                        345
                         */
                        buildElement<BaseItem>(0, 4, 5) { setItem(buildItem(Material.DIAMOND)) }
                        buildElement<BaseItem>(1, 2, 3) { setItem(buildItem(Material.EMERALD)) }

                    }

                }.also {
                    it.open()
                }

            submit(delay = 20L) {
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
            if (!isViewing()) cancel().also { return@submitAsync }
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