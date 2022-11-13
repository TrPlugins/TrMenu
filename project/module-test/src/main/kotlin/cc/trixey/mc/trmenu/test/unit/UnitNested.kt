package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.PagedNetesedPanel
import cc.trixey.mc.trmenu.invero.impl.panel.PagedStandardPanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.ContainerWindow
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.util.*
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.util.random
import taboolib.library.xseries.XSound

/**
 * @author Arasple
 * @since 2022/11/13 12:32
 */
object UnitNested {

    val command = subCommand {
        execute<Player> { player, _, _ ->
            /*
            *********
            *********
            *********
            *********
            *********
            <#######>
             */

            // Window
            val window = buildWindow<ContainerWindow>(player, TypeAddress.GENERIC_9X6, "Nested")

            // Nested Paged Panel
            val nested = buildPanel<PagedNetesedPanel>(9 to 5) {

                buildPanel<StandardPanel>(9 to 5) {
                    buildItem<BasicItem>(generateRandomItem()).set(*slotsUnoccupied.toIntArray())
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
                        page { index ->
                            previousPage.add(0)
                            nextPage.add(8)
                            randomFill().add(*slotsUnoccupied(index).toIntArray())
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
                buildItem<BasicItem>(Material.GRAY_STAINED_GLASS_PANE).set(*slotsUnoccupied.toIntArray())
            }

            window.apply {
                addPanel(nested)
                addPanel(nav)
            }.open()
        }
    }

}