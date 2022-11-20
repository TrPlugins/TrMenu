package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.common.WindowType
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.PagedNetesedPanel
import cc.trixey.mc.invero.panel.PagedStandardPanel
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.util.*
import cc.trixey.mc.invero.window.ContainerWindow
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XSound

/**
 * @author Arasple
 * @since 2022/11/13 12:32
 */
object UnitNested {

    val command = subCommand {
        execute { player, _, _ ->
            /*
            *********
            *********
            *********
            *********
            *********
            <#######>
             */

            // Window
            val window = buildWindow<ContainerWindow>(player, WindowType.GENERIC_9X6, "Nested")

            // Nested Paged Panel
            val nested = buildPanel<PagedNetesedPanel>(9 to 5) {

                buildPanel<StandardPanel>(9 to 5) {
                    buildItem<BasicItem>(generateRandomItem()).set(getUnoccupiedSlots())
                }.paged(this)

                buildPanel<PagedStandardPanel>(9 to 5) {
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
                    val randomFill: () -> BasicItem = { buildItem(generateRandomItem()) }
                    for (i in 0..10) {
                        page {
                            previousPage.set(0)
                            nextPage.set(8)
                            randomFill().set(*getUnoccupiedSlots(it).toIntArray())
                        }
                    }
                }.paged(this)

            }

            // Nav bar of Standard Panel
            // <......>
            val nav = buildPanel<StandardPanel>(9 to 1, nested.slots.last() + 1) {
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
                this += nested
                this += nav
            }.open()
        }
    }

}