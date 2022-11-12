package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.coroutine.launch
import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPagedPanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.CompleteWindow
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
 */
@CommandHeader(name = "testInvero")
object TestInvero {

    @CommandBody
    val testDynamicTitle = subCommand {
        execute { player, _, _ ->
            val window = buildWindow<CompleteWindow>(player, TypeAddress.GENERIC_9X3) {
                addPanel<StandardPanel>(9 to 3, 0) {
                    for (s in slots) addElement<BasicItem>(s) {
                        setItem(generateRandomItem())
                    }
                }
            }.also { it.open() }

            // Skedule 在低分辨率 （<3ticks) 下存在抛 “ Already resumed” 异常的问题
            // TODO 研究解决或用 Submit 代替低分辨率场景
            launch(true) {
                repeating(3)
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
            launchAsync {
                var count = 1

                val panel = buildPanel<StandardPanel>(3 to 10) {
                    addElement<BasicItem>(0, 4, 5) {
                        setItem(Material.DIAMOND)
                        onClick {
                            modify { amount = (if (isLeftClick) count++ else count--) }
                        }
                    }
                    addElement<BasicItem>(*slotsUnoccupied.toIntArray()) { setItem(Material.EMERALD) }
                }

                val pagedPanel = buildPanel<StandardPagedPanel>(3 to 8, 4) {
                    val previousPage = buildItem<BasicItem>(Material.ARROW, {
                        name = "§3Previous page"
                        lore.add("$pageIndex / $maxPageIndex")
                    }, {
                        onClick {
                            previousPage()
                            modifyLore { set(0, "$pageIndex / $maxPageIndex") }
                            forWindows { title = "Page: $pageIndex / $maxPageIndex" }
                        }
                    })
                    val nextPage = buildItem<BasicItem>(Material.ARROW, {
                        name = "§aNext Page"
                        lore.add("$pageIndex / $maxPageIndex")
                    }, {
                        onClick {
                            nextPage()
                            modifyLore { set(0, "$pageIndex / $maxPageIndex") }
                            forWindows { title = "Page: $pageIndex / $maxPageIndex" }
                        }
                    })
                    val fill = buildItem<BasicItem>(Material.BLACK_STAINED_GLASS)
                    val fill2 = buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE)
                    val randomFill: () -> BasicItem = { buildItem(generateRandomItem()) }

                    page { index ->
                        setElement(0, previousPage)
                        setElement(2, nextPage)

                        slotsUnoccupied(index).forEach { setElement(it, fill) }
                    }

                    page { index ->
                        setElement(0, previousPage)
                        setElement(2, nextPage)
                        slotsUnoccupied(index).forEach { setElement(it, fill2) }
                    }

                    for (i in 0..10) {
                        page { index ->
                            setElement(0, previousPage)
                            setElement(2, nextPage)
                            slotsUnoccupied(index).forEach { setElement(it, randomFill()) }
                        }
                    }
                }

                buildWindow<CompleteWindow>(player) {
                    title = "Standard Panels Test"

                    addPanel(
                        markPanel, panel, pagedPanel
                    )
                }.also { it.open() }
            }
        }
    }

    @CommandBody
    val testStandardPosMark = subCommand {
        execute<ProxyCommandSender> { sender, _, arguemnt ->
            sender.sendMessage("PosMarked")
            markPanel = testPanelPosMark(arguemnt.split(" ")[1].toIntOrNull() ?: 4)
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

    var markPanel: StandardPanel = testPanelPosMark(32)

    private fun generateRandomItem(): ItemStack {
        var itemStack: ItemStack? = null
        while (itemStack == null || itemStack.isAir) {
            itemStack = XMaterial.values().random().parseItem()
        }
        return itemStack
    }

    private fun testPanelPosMark(pos: Int): StandardPanel {
        return buildPanel(3 to 3, pos) {
            addElement<BasicItem>(*slots.toIntArray()) {
                setItem(Material.values().random())
            }
        }
    }

}