package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import cc.trixey.mc.trmenu.invero.impl.element.BaseItem
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPagedPanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.ContainerWindow
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.util.*
import org.bukkit.Material
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submitAsync

/**
 * @author Arasple
 * @since 2022/10/29 10:51
 *
 * TODO
 * [√] Window 交互与 Panel、PanelElement 的传递
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

    var preCreated: StandardPanel = testPanelGenerate(32)

    @CommandBody
    val release = subCommand {
        execute { player, _, _ ->
            var count = 1

            buildWindow<ContainerWindow>(player) {
                title = "Hello Invero"
                addPanel(preCreated)

                /**
                 * StandardPanel
                 */

                /**
                 * StandardPanel
                 */
                addPanel<StandardPanel>(3 to 2) {
                    addElement<BaseItem>(0, 4, 5) {
                        setItem(Material.DIAMOND)
                        onClick {
                            isCancelled = true
                            modify { amount = (if (isLeftClick) count++ else count--) }
                        }
                    }
                    addElement<BaseItem>(1, 2, 3) { setItem(Material.EMERALD) }
                }

                /**
                 * StandardPagedPanel
                 *
                 * *** ###
                 * *** ###
                 */

                /**
                 * StandardPagedPanel
                 *
                 * *** ###
                 * *** ###
                 */
                addPanel<StandardPagedPanel>(3 to 2) {
                    markPosition(4)

                    val previousPage = createElement<BaseItem> {
                        setItem(Material.ARROW) { name = "§3Previous page" }
                        onClick {
                            isCancelled = true
                            previousPage()
                            title = "Page: $pageIndex / $maxPageIndex"
                        }
                    }

                    val nextPage = createElement<BaseItem> {
                        setItem(Material.ARROW) { name = "§aNext page" }
                        onClick {
                            isCancelled = true
                            nextPage()
                            title = "Page: $pageIndex / $maxPageIndex"
                        }
                    }

                    val fill = createElement<BaseItem> {
                        setItem(Material.BLACK_STAINED_GLASS)
                    }

                    val fill2 = createElement<BaseItem> {
                        setItem(Material.LIME_STAINED_GLASS_PANE)
                    }

                    page {
                        setElement(0, previousPage)
                        setElement(2, nextPage)
                        slots.forEach {
                            if (it != 0 && it != 2) {
                                setElement(it, fill)
                            }
                        }
                    }

                    page {
                        setElement(0, previousPage)
                        setElement(2, nextPage)
                        slots.forEach {
                            if (it != 0 && it != 2) {
                                setElement(it, fill2)
                            }
                        }
                    }

                }
            }.also { it.open() }

//            submit(delay = 20L) { window.testAnimatedTitle() }
        }
    }

    @CommandBody
    val refreshTest = subCommand {
        execute<ProxyCommandSender> { sender, _, arguemnt ->
            sender.sendMessage("Refreshed")
            preCreated = testPanelGenerate(arguemnt.split(" ")[1].toIntOrNull() ?: 4)
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


    private fun testPanelGenerate(pos: Int = 4): StandardPanel {
        return createPanel {
            scale = 3 to 3
            markPosition(pos)

            addElement<BaseItem>(relativeSlot = slots.toIntArray()) {
                setItem(Material.values().random())
            }
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