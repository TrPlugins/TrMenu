package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.util.dsl.*
import cc.trixey.mc.trmenu.coroutine.launchAsync
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/11/20 18:30
 */
object UnitScroll {

    val vertical = subCommand {
        execute<Player> { player, _, _ ->
            launchAsync { player.showVertical() }
        }
    }

    val horizontal = subCommand {
        execute<Player> { player, _, _ ->
            launchAsync { player.showHorizontal() }
        }
    }

    fun Player.showVertical() {
        window(this, title = "Scroll Vertical") {

            scroll(8 to 6) {
                scrollVertically()
                scrollRandomType()
                if (type.isStop) stopDead()

                for (i in 0..16)
                    fillColum { item(generateRandomItem()) }
            }

            nav(1 to 6) {

                item(firstSlot(), Material.LIME_STAINED_GLASS_PANE, builder = { name = "§a↑ UP" }) {
                    onClick { getScrollPanel().previous() }
                }

                item(lastSlot(), Material.CYAN_STAINED_GLASS_PANE, builder = { name = "§3↓ DOWN" }) {
                    onClick { getScrollPanel().next() }
                }

                item(Material.GRAY_STAINED_GLASS_PANE).fillup()

            }

        }.open()
    }

    fun Player.showHorizontal() {
        window(this, title = "Scroll Horizontal") {

            scroll(9 to 5) {
                scrollHorizontally()
                scrollRandomType()

                for (i in 0..16)
                    fillColum { item(generateRandomItem()) }

            }

            nav(9 to 1) {

                item(firstSlot(), Material.LIME_STAINED_GLASS_PANE, builder = { name = "§a← LEFT" }) {
                    onClick { getScrollPanel().previous() }
                }

                item(lastSlot(), Material.CYAN_STAINED_GLASS_PANE, builder = { name = "§a→ RIGHT" }) {
                    onClick { getScrollPanel().next() }
                }

                item(Material.GRAY_STAINED_GLASS_PANE).fillup()

            }

        }.open()
    }

}