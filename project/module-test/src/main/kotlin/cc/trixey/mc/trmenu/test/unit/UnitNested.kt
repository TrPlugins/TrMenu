package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.impl.container.element.BasicItem
import cc.trixey.mc.invero.impl.container.util.dsl.*
import cc.trixey.mc.invero.impl.container.util.dsl.nav
import cc.trixey.mc.invero.impl.container.util.addGroup
import cc.trixey.mc.invero.impl.container.util.page
import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XSound
import taboolib.platform.util.Slots

/**
 * @author Arasple
 * @since 2022/11/13 12:32
 */
object UnitNested {

    fun Player.showNetesed() = completeWindow(this, title = "Netesed") {

        /*
        嵌套翻页 UI
         */
        pagedNetesed(9 to 6) {

            // 嵌套第一页 standard
            standard(scale.pair) {
                item(generateRandomItem()).fillup()
            }

            // 嵌套第二页 paged
            paged(scale.pair) {
                background {
                    val controlItem: (name: String, shift: Int) -> BasicItem = { name, shift ->
                        item(Material.ARROW, {
                            this.name = name
                            lore.add("$pageIndex / $maxPageIndex")
                        }, {
                            onClick {
                                shiftPage(shift)
                                title = "Page: $pageIndex / $maxPageIndex"
                            }
                        })
                    }

                    controlItem("§aNext Page", 1).set(8)
                    controlItem("§3Previous Page", -1).set(0)
                }

                for (i in 0..10) page {
                    item(generateRandomItem()).fillup()
                }

            }

            // 嵌套第三页 {ScrollGeneratorPanel, StandardPanel}
            addGroup(scale.pair) {

                nav(9 to 1, 0) {
                    item(firstSlot(), Material.LIME_STAINED_GLASS_PANE, builder = { name = "§a← LEFT" }) {
                        onClick { getScrollPanel().previous() }
                    }

                    item(lastSlot(), Material.CYAN_STAINED_GLASS_PANE, builder = { name = "§a→ RIGHT" }) {
                        onClick { getScrollPanel().next() }
                    }

                    item(Material.BLACK_STAINED_GLASS_PANE).fillup()
                }

                generatorScroll(9 to 5) {
                    scrollHorizontally()
                    loop()

                    generator(UnitGenerator.data) { player ->
                        item(
                            generateRandomItem {
                                name = "§3${player.name}"
                                lore.addAll(
                                    arrayOf(
                                        "§r",
                                        "§cHealth: ${player.health}",
                                        "§2UUID: ${player.uuid}",
                                        "§r"
                                    )
                                )
                            }
                        )
                    }
                }

            }

        }

        /*
        嵌套 UI 的导航条
         */
        nav(9 to 1, Slots.LINE_6_RIGHT + 1) {
            val nested = getPagedNetesedPanel()

            item(0, Material.CYAN_STAINED_GLASS_PANE, { name = "§3Previous page" }, {
                onClick {
                    nested.previousPage()
                    title = "Nested ${nested.pageIndex} / ${nested.maxPageIndex}"
                    XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                }
            })

            item(8, Material.LIME_STAINED_GLASS_PANE, { name = "§aNext page" }, {
                onClick {
                    nested.nextPage()
                    title = "Nested ${nested.pageIndex} / ${nested.maxPageIndex}"
                    XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                }
            })

            item(Material.GRAY_STAINED_GLASS_PANE).fillup()
        }

        open()
    }

    val command = subCommand {
        execute<Player> { player, _, _ ->
            launchAsync { player.showNetesed() }
        }
    }

}