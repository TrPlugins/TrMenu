package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.trmenu.invero.impl.element.BasicDynamicItem
import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPagedPanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.CompleteWindow
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.util.*
import cc.trixey.mc.trmenu.test.TestInvero
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
                buildWindow<CompleteWindow>(player, TypeAddress.GENERIC_9X6) {
                    addPanel(it)
                }.also { it.open() }
            }
        }
    }

    val paged = subCommand {
        execute<Player> { player, _, _ ->
            buildPanel<StandardPagedPanel>(9 to 6) {
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
                        previousPage.add(0)
                        nextPage.add(8)

                        var count = 9
                        val rand = buildItem<BasicDynamicItem>(TestInvero.generateRandomItem()) {
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
                buildWindow<CompleteWindow>(player, TypeAddress.GENERIC_9X6) {
                    addPanel(it)
                }.also { it.open() }
            }
        }
    }

}