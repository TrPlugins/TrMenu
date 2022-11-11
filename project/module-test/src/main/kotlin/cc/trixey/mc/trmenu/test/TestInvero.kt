package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.coroutine.launch
import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import cc.trixey.mc.trmenu.invero.impl.element.BaseItem
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPagedPanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.CompleteWindow
import cc.trixey.mc.trmenu.invero.impl.window.ContainerWindow
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.util.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.isAir

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

    @CommandBody
    val testDynamicTitle = subCommand {
        execute { player, _, _ ->
            val window = buildWindow<CompleteWindow>(player, TypeAddress.GENERIC_9X3) {
                addPanel<StandardPanel>(9 to 3, 0) {
                    for (s in 0..27) addElement<BaseItem>(s) {
                        setItem(generateRandomItem())
                    }
                }
            }.also { it.open() }

            launch(true) {
                // Skedule 在低分辨率 （<3ticks) 下存在抛 “ Already resumed” 异常的问题
                // TODO 研究解决或用 Submit 代替低分辨率场景
                repeating(5)
                for (title in dynamicTitles) {
                    window.title = title
                    yield()
                }
                window.title = "Hello Invero"
            }
        }
    }

    @CommandBody
    val testStandard = subCommand {
        execute { player, _, _ ->
            var count = 1

            buildWindow<ContainerWindow>(player) {
                title = "Hello Invero"

                addPanel(posMarkPanel)
                addPanel<StandardPanel>(3 to 2) {
                    addElement<BaseItem>(0, 4, 5) {
                        setItem(Material.DIAMOND)
                        onClick {
                            modify { amount = (if (isLeftClick) count++ else count--) }
                        }
                    }
                    addElement<BaseItem>(1, 2, 3) { setItem(Material.EMERALD) }
                }
                addPanel<StandardPagedPanel>(3 to 2, 4) {
                    val previousPage = createItem<BaseItem>(Material.ARROW, { name = "§3Previous page" }, {
                        onClick {
                            previousPage()
                            title = "Page: $pageIndex / $maxPageIndex"
                        }
                    })
                    val nextPage = createItem<BaseItem>(Material.ARROW, { name = "§aNext Page" }, {
                        onClick {
                            nextPage()
                            title = "Page: $pageIndex / $maxPageIndex"
                        }
                    })
                    val fill = createItem<BaseItem>(Material.BLACK_STAINED_GLASS)
                    val fill2 = createItem<BaseItem>(Material.LIME_STAINED_GLASS_PANE)

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
        }
    }

    @CommandBody
    val testStandardPosMark = subCommand {
        execute<ProxyCommandSender> { sender, _, arguemnt ->
            sender.sendMessage("PosMarked")
            posMarkPanel = testPanelPosMark(arguemnt.split(" ")[1].toIntOrNull() ?: 4)
        }
    }


    @CommandBody
    val print = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendMessage("Size: ${WindowHolder.runningWindows.size}")
            WindowHolder.runningWindows.forEach {
                sender.sendMessage(":Window: ${it.type}")
            }
        }
    }

    private val dynamicTitles by lazy {
        var current = ""
        val titles = mutableListOf<String>()

        "Invero Animated Title".windowed(1, 1).forEachIndexed { _, s ->
            current += s
            titles.add(current)
        }
        titles.addAll(titles.reversed())
        titles
    }

    var posMarkPanel: StandardPanel = testPanelPosMark(32)

    private fun generateRandomItem(): ItemStack {
        var itemStack: ItemStack? = null
        while (itemStack == null || itemStack.isAir) {
            itemStack = XMaterial.values().random().parseItem()
        }
        return itemStack
    }

    private fun testPanelPosMark(pos: Int): StandardPanel {
        return createPanel {
            scale = 3 to 3
            markPosition(pos)

            addElement<BaseItem>(relativeSlot = slots.toIntArray()) {
                setItem(Material.values().random())
            }
        }
    }

}