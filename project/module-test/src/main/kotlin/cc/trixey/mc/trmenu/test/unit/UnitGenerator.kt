package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.util.dsl.*
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.library.xseries.XSound
import taboolib.platform.util.Slots
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/13 16:16
 */
object UnitGenerator {

    internal val data = arrayListOf(
        TestPlayer("John Snow"), TestPlayer("Daenerys Targaryen"), TestPlayer("Daemon Targaryen")
    ).also {
        for (i in 0..200) it += TestPlayer("Random#$i")
    }

    internal data class TestPlayer(
        val name: String, val health: Int = (0..20).random(), val uuid: UUID = UUID.randomUUID()
    )

    fun pagedGenerator(player: Player) {
        completeWindow(player) {

            generatorPaged(9 to 6) {

                generator(data) { player ->
                    item(generateRandomItem {
                        name = "§3${player.name}"
                        lore.addAll(listOf("§r", "§cHealth: ${player.health}", "§2UUID: ${player.uuid}", "§r"))
                    })
                }

                background {
                    item(Material.CYAN_STAINED_GLASS_PANE, { name = "§3Previous page" }, {
                        onClick {
                            previousPage()
                            title = "Generator $pageIndex / $maxPageIndex"
                            XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                        }
                    }).set(Slots.LINE_6_LEFT)

                    item(Material.LIME_STAINED_GLASS_PANE, { name = "§aNext page" }, {
                        onClick {
                            nextPage()
                            title = "Generator $pageIndex / $maxPageIndex"
                            XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                        }
                    }).set(Slots.LINE_6_RIGHT)
                }
            }

        }.open()
    }

    fun scrollGenerator(player: Player) {
        completeWindow(player) {

            generatorScroll(9 to 5) {
                generator(data) { player ->
                    item(generateRandomItem {
                        name = "§3${player.name}"
                        lore.addAll(listOf("§r", "§cHealth: ${player.health}", "§2UUID: ${player.uuid}", "§r"))
                    })
                }
            }

            nav(9 to 1) {
                val scroll = getScrollPanel()
                item(firstSlot(), Material.CYAN_STAINED_GLASS_PANE, { name = "§3← LEFT" }, {
                    onClick {
                        scroll.previous()
                        title = "ScrollGen ${scroll.index} / ${scroll.maxIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                })
                item(lastSlot(), Material.LIME_STAINED_GLASS_PANE, { name = "§a→ RIGHT" }, {
                    onClick {
                        scroll.next()
                        title = "ScrollGen ${scroll.index} / ${scroll.maxIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                })
                item(Material.GRAY_STAINED_GLASS_PANE).fillup()
            }

        }.open()
    }

}