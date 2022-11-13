package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.PagedStandardPanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.CompleteWindow
import cc.trixey.mc.trmenu.invero.util.*
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/11/12 20:53
 */
object UnitStandard {

    val command = subCommand {
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

                val pagedPanel = buildPanel<PagedStandardPanel>(3 to 8, 4) {
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

    val posMark = subCommand {
        execute<ProxyCommandSender> { sender, _, arguemnt ->
            sender.sendMessage("PosMarked")
            markPanel = testPanelPosMark(arguemnt.split(" ")[1].toIntOrNull() ?: 4)
        }
    }

    private var markPanel: StandardPanel = testPanelPosMark(32)

    private fun testPanelPosMark(pos: Int): StandardPanel {
        return buildPanel(3 to 3, pos) {
            addElement<BasicItem>(*slots.toIntArray()) {
                setItem(Material.values().random())
            }
        }
    }

}