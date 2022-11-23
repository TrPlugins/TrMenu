package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.util.addElement
import cc.trixey.mc.invero.util.buildPanel
import cc.trixey.mc.invero.util.dsl.item
import cc.trixey.mc.invero.util.dsl.paged
import cc.trixey.mc.invero.util.dsl.standard
import cc.trixey.mc.invero.util.dsl.window
import cc.trixey.mc.invero.util.page
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
                item(org.bukkit.Material.DIAMOND).set(0, 4, 5)
                item(org.bukkit.Material.EMERALD).fillup()
            }

            paged(3 to 10, 4) {

                background {
                    val controlItem: (name: String, shift: Int) -> BasicItem = { name, shift ->
                        item(
                            org.bukkit.Material.ARROW,
                            {
                                this.name = name
                                lore.add("$pageIndex / $maxPageIndex")
                            },
                            {
                                onClick {
                                    shiftPage(shift)
                                    title = "Page: $pageIndex / $maxPageIndex"
                                }
                            }
                        )
                    }

                    controlItem("§aNext Page", 1).set(2)
                    controlItem("§3Previous Page", 0).set(0)
                }

                val randFill = { item(generateRandomItem()) }

                page {
                    item(org.bukkit.Material.BLACK_STAINED_GLASS).set(getUnoccupiedSlots(it))
                }
                page {
                    item(org.bukkit.Material.LIME_STAINED_GLASS_PANE).set(getUnoccupiedSlots(it))
                }

                for (i in 0..10)
                    page { randFill().set(getUnoccupiedSlots(it)) }

            }

            this += cc.trixey.mc.trmenu.test.unit.UnitStandard.markedPanel
        }
    }


    val command = subCommand {
        execute<Player> { player, _, _ ->
            launchAsync { player.showStandard() }
        }
    }

    val posMark = subCommand {
        execute<ProxyCommandSender> { sender, _, arguemnt ->
            sender.sendMessage("PosMarked")
            markedPanel = testPanelPosMark(arguemnt.split(" ")[1].toIntOrNull() ?: 4)
        }
    }

    private var markedPanel: StandardPanel = testPanelPosMark(8)

    private fun testPanelPosMark(pos: Int): StandardPanel {
        return buildPanel(1 to 8, pos, PanelWeight.BACKGROUND) {
            addElement<BasicItem>(slots) {
                setItem(Material.values().random())
            }
        }
    }

}