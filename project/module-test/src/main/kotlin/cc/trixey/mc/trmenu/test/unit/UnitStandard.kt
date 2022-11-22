package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.PagedStandardPanel
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.window.CompleteWindow
import cc.trixey.mc.invero.util.*
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

                val panel = buildPanel<StandardPanel>(3 to 5) {
                    buildItem<BasicItem>(Material.DIAMOND) {
                        onClick {
                            modify { amount = (if (isLeftClick) count++ else count--) }
                        }
                    }.set(0, 4, 5)
                    buildItem<BasicItem>(Material.EMERALD).set(getUnoccupiedSlots())
                }

                val pagedPanel = buildPanel<PagedStandardPanel>(3 to 10, 4) {

                    background {
                        buildItem<BasicItem>(Material.ARROW, {
                            name = "§3Previous page"
                            lore.add("$pageIndex / $maxPageIndex")
                        }, {
                            onClick {
                                previousPage()
                                modifyLore { set(0, "$pageIndex / $maxPageIndex") }
                                forWindows { title = "Page: $pageIndex / $maxPageIndex" }
                            }
                        }).set(0)

                        buildItem<BasicItem>(Material.ARROW, {
                            name = "§aNext Page"
                            lore.add("$pageIndex / $maxPageIndex")
                        }, {
                            onClick {
                                nextPage()
                                modifyLore { set(0, "$pageIndex / $maxPageIndex") }
                                forWindows { title = "Page: $pageIndex / $maxPageIndex" }
                            }
                        }).set(2)
                    }

                    val fill: () -> BasicItem = { buildItem(generateRandomItem()) }

                    page {
                        add(getUnoccupiedSlots(it), buildItem<BasicItem>(Material.BLACK_STAINED_GLASS))
                    }
                    page {
                        add(getUnoccupiedSlots(it), buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE))
                    }

                    for (i in 0..10)
                        page { add(getUnoccupiedSlots(it), fill()) }
                }

                buildWindow<CompleteWindow>(player) {
                    title = "Standard Panels Test"

                    this += panel
                    this += pagedPanel
                    this += markedPanel
                }.also { it.open() }
            }
        }
    }

    val posMark = subCommand {
        execute<ProxyCommandSender> { sender, _, arguemnt ->
            sender.sendMessage("PosMarked")
            markedPanel = testPanelPosMark(arguemnt.split(" ")[1].toIntOrNull() ?: 4)
        }
    }

    private var markedPanel: StandardPanel = testPanelPosMark(8)

    private fun testPanelPosMark(pos: Int): StandardPanel {
        return buildPanel(1 to 8, pos,PanelWeight.BACKGROUND) {
            addElement<BasicItem>(slots) {
                setItem(Material.values().random())
            }
        }
    }

}