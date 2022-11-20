package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.ScrollStandardPanel
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.util.buildItem
import cc.trixey.mc.invero.util.buildPanel
import cc.trixey.mc.invero.util.buildWindow
import cc.trixey.mc.invero.window.ContainerWindow
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XSound
import taboolib.platform.util.Slots

/**
 * @author Arasple
 * @since 2022/11/20 18:30
 */
object UnitScroll {

    val standard = subCommand {
        execute<Player> { player, _, _ ->
            val scroll = buildPanel<ScrollStandardPanel>(8 to 6) {
                for (colum in 0..16) {
                    colum {
                        val item = buildItem<BasicItem>(generateRandomItem())
                        for (i in 0..7) this[i] = item
                    }
                }
            }

            val nav = buildPanel<StandardPanel>(1 to 6, Slots.LINE_1_RIGHT) {
                buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE, {
                    name = "&a↑ UP"
                    colored()
                }, {
                    onClick {
                        scroll.scroll(-1)
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(player)
                    }
                }).set(0)

                buildItem<BasicItem>(Material.CYAN_STAINED_GLASS_PANE, {
                    name = "&3↓ DOWN"
                    colored()
                }, {
                    onClick {
                        scroll.scroll(1)
                        XSound.BLOCK_NOTE_BLOCK_PLING.play(player)
                    }
                }).set(5)

                buildItem<BasicItem>(Material.GRAY_STAINED_GLASS_PANE).set(getUnoccupiedSlots())
            }

            buildWindow<ContainerWindow>(player, title = "Scroll Normal") {
                this += scroll
                this += nav
            }.open()
        }
    }

}