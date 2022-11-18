package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.PagedGeneratorPanel
import cc.trixey.mc.trmenu.invero.impl.window.CompleteWindow
import cc.trixey.mc.trmenu.invero.module.Generator
import cc.trixey.mc.trmenu.invero.util.buildItem
import cc.trixey.mc.trmenu.invero.util.buildPanel
import cc.trixey.mc.trmenu.invero.util.buildWindow
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XSound
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/13 16:16
 */
object UnitGenerator {

    // Data for Generator
    private val data = arrayListOf(
        TestPlayer("John Snow"), TestPlayer("Daenerys Targaryen"), TestPlayer("Daemon Targaryen")
    ).also {
        for (i in 0..200) it += TestPlayer("Random#$i")
    }

    // Data of Items for Generator
    private val items = buildList {
        for (i in data.indices) {
            val da = data[i]
            add(i, generateRandomItem {
                name = "§3${da.name}"
                lore.addAll(listOf("§r", "§cHealth: ${da.health}", "§2UUID: ${da.uuid}", "§r"))
            })
        }
    }

    // Data class
    data class TestPlayer(
        val name: String, val health: Int = (0..20).random(), val uuid: UUID = UUID.randomUUID()
    )

    val command = subCommand {
        execute<Player> { player, _, _ ->
            val window = buildWindow<CompleteWindow>(player)

            // Gen tests
            val gen = buildPanel<PagedGeneratorPanel>(9 to 5) {
                generator = Generator(data.size) {
                    val data = data[it]
                    return@Generator buildItem<BasicItem>(items[it]) {
                        onClick { player.sendMessage(data.toString()) }
                    }
                }

                // Nav bar
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

}