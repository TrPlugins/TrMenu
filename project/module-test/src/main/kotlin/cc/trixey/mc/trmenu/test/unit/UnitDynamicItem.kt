package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.WindowType
import cc.trixey.mc.invero.impl.container.element.BasicDynamicItem
import cc.trixey.mc.invero.impl.container.element.BasicItem
import cc.trixey.mc.invero.impl.container.panel.PagedStandardPanel
import cc.trixey.mc.invero.impl.container.panel.StandardPanel
import cc.trixey.mc.invero.impl.container.util.buildItem
import cc.trixey.mc.invero.impl.container.util.buildPanel
import cc.trixey.mc.invero.impl.container.util.buildWindow
import cc.trixey.mc.invero.impl.container.util.page
import cc.trixey.mc.invero.impl.container.window.CompleteWindow
import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submitAsync

/**
 * @author Arasple
 * @since 2022/11/12 20:51
 */
object UnitDynamicItem {

    val normal = subCommand {
        execute<Player> { player, _, _ ->
            buildPanel<StandardPanel>(9 to 6) {
                val dynamicDiamond = buildItem<BasicDynamicItem>(Material.DIAMOND) {
                    slots(0)
                    onClick {
                        modify { amount++ }
                        player.sendMessage("You clicked $slot, Amount++")
                    }
                }.add()

                launchAsync {
                    repeating(10)
                    for (i in 0..60) {
                        dynamicDiamond.slots(i, i + 1, i + 2)
                        yield()
                    }
                    dynamicDiamond.slots(0)
                }
            }.let {
                buildWindow<CompleteWindow>(player, WindowType.GENERIC_9X6) {
                     add(it)
                }.also { it.open() }
            }
        }
    }

    val paged = subCommand {
        execute<Player> { player, _, _ ->
            buildPanel<PagedStandardPanel>(9 to 6) {
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

                for (i in 0..3) {
                    page { currentPage ->
                        previousPage.set(0)
                        nextPage.set(8)

                        var count = 9
                        val rand = buildItem<BasicDynamicItem>(generateRandomItem()) {
                            slots(9)
                            onClick {
                                modify { amount++ }
                                player.sendMessage("You clicked $slot on page $pageIndex, Amount++")
                            }
                        }.add()

                        player.sendMessage("Wait 2 secs")
                        submitAsync(delay = 2 * 20, period = 8) {
                            if (pageIndex == currentPage) {
                                if (count <= 60) {
                                    rand.slots(count, count + 1, count + 2)
                                    count++
                                } else {
                                    cancel()
                                    rand.slots(9)
                                }
                            }
                        }
                    }
                }
            }.let {
                buildWindow<CompleteWindow>(player, WindowType.GENERIC_9X6) {
                     add(it)
                }.also { it.open() }
            }
        }
    }

}