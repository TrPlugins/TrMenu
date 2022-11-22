package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.PagedGeneratorPanel
import cc.trixey.mc.invero.panel.ScrollGeneratorPanel
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.util.buildItem
import cc.trixey.mc.invero.util.buildPanel
import cc.trixey.mc.invero.util.buildWindow
import cc.trixey.mc.invero.window.CompleteWindow
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XSound
import taboolib.platform.util.Slots
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/13 16:16
 */
object UnitGenerator {

    private val data = arrayListOf(
        TestPlayer("John Snow"),
        TestPlayer("Daenerys Targaryen"),
        TestPlayer("Daemon Targaryen")
    ).also {
        for (i in 0..200) it += TestPlayer("Random#$i")
    }

    private data class TestPlayer(
        val name: String,
        val health: Int = (0..20).random(),
        val uuid: UUID = UUID.randomUUID()
    )

    val paged = subCommand {
        execute { player, _, _ ->
            val window = buildWindow<CompleteWindow>(player)

            val gen = buildPanel<PagedGeneratorPanel>(9 to 5) {

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
                    }).set(0)
                    buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE, { name = "§aNext page" }, {
                        onClick {
                            nextPage()
                            window.title = "Gen $pageIndex / $maxPageIndex"
                            XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                        }
                    }).set(8)
                }
            }
            window.apply {
                this += gen
                open()
            }
        }
    }

    val scroll = subCommand {
        execute { player, _, _ ->
            val window = buildWindow<CompleteWindow>(player)

            val gen = buildPanel<ScrollGeneratorPanel>(9 to 5) {
                direction = ScrollDirection.HORIZONTAL
                type = ScrollType.LOOP

                generator<BasicItem, TestPlayer>(data) { player ->
                    buildItem(generateRandomItem {
                        name = "§3${player.name}"
                        lore.addAll(listOf("§r", "§cHealth: ${player.health}", "§2UUID: ${player.uuid}", "§r"))
                    })
                }
            }

            val nav = buildPanel<StandardPanel>(9 to 1, Slots.LINE_6_LEFT) {
                buildItem<BasicItem>(Material.CYAN_STAINED_GLASS_PANE, { name = "§3← LEFT" }, {
                    onClick {
                        gen.scroll(-1)
                        window.title = "ScrollGen ${gen.index} / ${gen.maxIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(0)
                buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE, { name = "§a→ RIGHT" }, {
                    onClick {
                        gen.scroll(1)
                        window.title = "ScrollGen ${gen.index} / ${gen.maxIndex}"
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(whoClicked)
                    }
                }).set(8)
                buildItem<BasicItem>(Material.GRAY_STAINED_GLASS_PANE).set(getUnoccupiedSlots())
            }

            window.apply {
                this += gen
                this += nav
                open()
            }
        }
    }

}