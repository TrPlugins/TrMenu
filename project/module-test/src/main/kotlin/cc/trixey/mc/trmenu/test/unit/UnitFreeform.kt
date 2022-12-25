package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.impl.container.element.BasicDynamicItem
import cc.trixey.mc.invero.impl.container.util.dsl.*
import cc.trixey.mc.invero.impl.container.util.buildItem
import cc.trixey.mc.trmenu.coroutine.launchAsync
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/11/24 15:23
 */
object UnitFreeform {

    private fun Player.showNetesedFreeform() {
        completeWindow(this, title = "Netesed Freeform") {

            freeformNetesed(9 to 6) {

                standard(3 to 3, 0) {
                    item(Material.STONE).onClick {
                        val lowered = slot.toLowerSlot()
                        val uppered = lowered?.toUpperSlot()
                        println("-------------------------")
                        println("RAW: $slot")
                        println("RAW/LOWERED/UPPERED: $slot / $lowered $uppered")
                        println("${scale.getCache()}")
                    }.fillup()
                }

                standard(3 to 3, 4) {

                    val diamond = buildItem<BasicDynamicItem>(Material.DIAMOND) {
                        slots(0)
                        onClick {
                            modify { amount++ }
                            player?.sendMessage("You clicked $slot, Amount++")
                        }
                    }.add()
                    var i = 0


                    launchAsync {
                        repeating(10)


                        for (trial in 0..1000) {
                            diamond.slots(scale.slots[i++])
                            if (i > 8) i = 0
                            yield()
                        }

                        diamond.slots(0)
                    }
                }

            }

            nav(3 to 3, 57) {
                val freeform = getFreeformNetesed()

                item(0, Material.GRAY_STAINED_GLASS_PANE, { name = "upLeft" }).onClick {
                    freeform.upLeft()
                }

                item(1, Material.LIME_STAINED_GLASS_PANE, { name = "UP" }).onClick {
                    freeform.up()
                }

                item(2, Material.GRAY_STAINED_GLASS_PANE, { name = "upRight" }).onClick {
                    freeform.upRight()
                }

                item(3, Material.RED_STAINED_GLASS_PANE, { name = "LEFT" }).onClick {
                    freeform.left()
                }

                item(4, Material.REDSTONE, { name = "RESET" }).onClick {
                    freeform.reset()
                }

                item(5, Material.YELLOW_STAINED_GLASS_PANE, { name = "RIGHT" }).onClick {
                    freeform.right()
                }

                item(6, Material.BLACK_STAINED_GLASS_PANE, { name = "downLeft" }).onClick {
                    freeform.downLeft()
                }

                item(7, Material.BLUE_STAINED_GLASS_PANE, { name = "DOWN" }).onClick {
                    freeform.down()
                }

                item(8, Material.BLACK_STAINED_GLASS_PANE, { name = "downRight" }).onClick {
                    freeform.downRight()
                }

            }

        }.open()
    }

    val netesed = subCommand {
        execute<Player> { player, _, _ ->
            player.showNetesedFreeform()
        }
    }

}