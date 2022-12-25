package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.PanelWeight
import cc.trixey.mc.invero.impl.container.element.BasicItem
import cc.trixey.mc.invero.impl.container.panel.StandardPanel
import cc.trixey.mc.invero.impl.container.util.dsl.item
import cc.trixey.mc.invero.impl.container.util.dsl.paged
import cc.trixey.mc.invero.impl.container.util.dsl.standard
import cc.trixey.mc.invero.impl.container.util.dsl.window
import cc.trixey.mc.invero.impl.container.util.page
import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/11/12 20:53
 */
object UnitStandard {

    private fun Player.showStandard() {
        window(this, title = "Standard") {
            standard(3 to 5) {
                item(Material.DIAMOND).set(0, 4, 5)
                item(Material.EMERALD).fillup()
            }

            paged(3 to 10, 4) {

                background {
                    val controlItem: (name: String, shift: Int) -> BasicItem = { name, shift ->
                        item(
                            Material.ARROW, { this.name = name }, {
                                onClick {
                                    shiftPage(shift)
                                    title = "Page: $pageIndex / $maxPageIndex"
                                }
                            }
                        )
                    }

                    controlItem("§aNext Page", 1).set(2)
                    controlItem("§3Previous Page", -1).set(0)
                }

                val randFill = { item(generateRandomItem()) }

                page {
                    item(Material.BLACK_STAINED_GLASS).fillup()
                }
                page {
                    item(Material.LIME_STAINED_GLASS_PANE).fillup()
                }

                for (i in 0..10)
                    page { randFill().fillup() }

            }
            this += markedPanel

            open()
        }
    }


    val command = subCommand {
        execute<Player> { player, _, _ ->
            launchAsync { player.showStandard() }
        }
    }

    private var markedPanel: StandardPanel = testPanelPosMark(8)

    val posMark = subCommand {
        execute<ProxyCommandSender> { sender, _, arguemnt ->
            val pos = arguemnt.split(" ")[1].toIntOrNull() ?: 4
            sender.sendMessage("PosMarked $pos")
            markedPanel = testPanelPosMark(pos)
        }
    }

    private fun testPanelPosMark(pos: Int) =
        standard(1 to 8, pos, PanelWeight.BACKGROUND) {
            item(generateRandomItem()).fillup()
        }

}