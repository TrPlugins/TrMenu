package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.PagedGeneratorPanel
import cc.trixey.mc.invero.panel.ScrollGeneratorPanel
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.util.*
import cc.trixey.mc.invero.window.CompleteWindow
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
        val window = buildWindow<CompleteWindow>(player)

        val gen = buildPanel<PagedGeneratorPanel>(9 to 6) {

            generator<BasicItem, TestPlayer>(data) { player ->
                buildItem(generateRandomItem {
                    name = "§3${player.name}"
                    lore.addAll(listOf("§r", "§cHealth: ${player.health}", "§2UUID: ${player.uuid}", "§r"))
                })
            }

            background {
                buildItem<BasicItem>(Material.CYAN_STAINED_GLASS_PANE, { name = "§3Previous page" }, {
                    onClick {
                        previousPage()
                        window.title = "Gen $pageIndex / $maxPageIndex"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(Slots.LINE_6_LEFT)
                buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE, { name = "§aNext page" }, {
                    onClick {
                        nextPage()
                        window.title = "Gen $pageIndex / $maxPageIndex"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(Slots.LINE_6_RIGHT)
            }
        }
        window.apply {
            this += gen
            open()
        }
    }

    fun scrollGenerator(player: Player) {
        buildWindow<CompleteWindow>(player) {

            val scroll = addPanel<ScrollGeneratorPanel>(9 to 5) {
                direction = ScrollDirection.HORIZONTAL
                type = ScrollType.LOOP

                generator<BasicItem, TestPlayer>(data) { player ->
                    buildItem(generateRandomItem {
                        name = "§3${player.name}"
                        lore.addAll(listOf("§r", "§cHealth: ${player.health}", "§2UUID: ${player.uuid}", "§r"))
                    })
                }
            }

            addPanel<StandardPanel>(9 to 1, Slots.LINE_6_LEFT) {
                buildItem<BasicItem>(Material.CYAN_STAINED_GLASS_PANE, { name = "§3← LEFT" }, {
                    onClick {
                        scroll.scroll(-1)
                        title = "ScrollGen ${scroll.index} / ${scroll.maxIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(0)
                buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE, { name = "§a→ RIGHT" }, {
                    onClick {
                        scroll.scroll(1)
                        title = "ScrollGen ${scroll.index} / ${scroll.maxIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(8)
                addElement<BasicItem>(getUnoccupiedSlots()) { setItem(Material.GRAY_STAINED_GLASS_PANE) }
            }

            open()
        }
    }

}