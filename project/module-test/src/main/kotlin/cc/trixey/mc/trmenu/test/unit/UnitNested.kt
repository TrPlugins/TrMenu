package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.WindowType
import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.PagedNetesedPanel
import cc.trixey.mc.invero.panel.PagedStandardPanel
import cc.trixey.mc.invero.panel.ScrollGeneratorPanel
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.util.*
import cc.trixey.mc.invero.window.ContainerWindow
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XSound
import taboolib.platform.util.Slots

/**
 * @author Arasple
 * @since 2022/11/13 12:32
 */
object UnitNested {

    val command = subCommand {
        execute { player, _, _ ->
            // Window
            val window = buildWindow<ContainerWindow>(player, WindowType.GENERIC_9X6, "Nested")

            // 9x5 的嵌套面板
            val nested = buildPanel<PagedNetesedPanel>(9 to 5) {

                // 嵌套第一页 StandardPanel
                addPanel<StandardPanel>(9 to 5) {
                    buildItem<BasicItem>(generateRandomItem()).set(getUnoccupiedSlots())
                }

                // 嵌套第二页 PagedStandardPanel
                addPanel<PagedStandardPanel>(9 to 5) {

                    // PagedStandardPanel 的静态占用元素（用作导航按钮）
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
                        }).set(8)
                    }

                    // PagedStandardPanel 随机生成的页面
                    for (i in 0..10) page { buildItem<BasicItem>(generateRandomItem()).set(getUnoccupiedSlots(it)) }

                }

                // 嵌套第三页 {ScrollGeneratorPanel, StandardPanel}
                addGroup(9 to 5) {

                    // 滚动面板 ScrollGeneratorPanel
                    val scroll = addPanel<ScrollGeneratorPanel>(9 to 4, Slots.LINE_2_LEFT) {
                        direction = ScrollDirection.HORIZONTAL
                        type = ScrollType.LOOP

                        generator<BasicItem, UnitGenerator.TestPlayer>(UnitGenerator.data) { player ->
                            buildItem(generateRandomItem {
                                name = "§3${player.name}"
                                lore.addAll(listOf("§r", "§cHealth: ${player.health}", "§2UUID: ${player.uuid}", "§r"))
                            })
                        }
                    }

                    // StandardPanel 作滚动面板的导航
                    addPanel<StandardPanel>(9 to 1, Slots.LINE_1_LEFT) {
                        buildItem<BasicItem>(Material.CYAN_STAINED_GLASS_PANE, { name = "§3← LEFT" }, {
                            onClick {
                                scroll.scroll(-1)
                                window.title = "ScrollGen ${scroll.index} / ${scroll.maxIndex}"
                                XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                            }
                        }).set(0)
                        buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE, { name = "§a→ RIGHT" }, {
                            onClick {
                                scroll.scroll(1)
                                window.title = "ScrollGen ${scroll.index} / ${scroll.maxIndex}"
                                XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                            }
                        }).set(8)
                        addElement<BasicItem>(getUnoccupiedSlots()) { setItem(Material.GRAY_STAINED_GLASS_PANE) }
                    }

                }

            }

            // 嵌套面板的导航 StandardPanel

            val nav = buildPanel<StandardPanel>(9 to 1, nested.slots.last() + 1, PanelWeight.SURFACE) {
                buildItem<BasicItem>(Material.CYAN_STAINED_GLASS_PANE, { name = "§3Previous page" }, {
                    onClick {
                        nested.previousPage()
                        window.title = "Nested ${nested.pageIndex} / ${nested.maxPageIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(0)
                buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE, { name = "§aNext page" }, {
                    onClick {
                        nested.nextPage()
                        window.title = "Nested ${nested.pageIndex} / ${nested.maxPageIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(8)
                buildItem<BasicItem>(Material.GRAY_STAINED_GLASS_PANE).set(getUnoccupiedSlots())
            }

            window.apply {
                this += nav
                this += nested
            }.open()
        }
    }

}