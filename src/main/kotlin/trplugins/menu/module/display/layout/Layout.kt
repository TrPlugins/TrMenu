package trplugins.menu.module.display.layout

import org.bukkit.event.inventory.InventoryType
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.submit
import trplugins.menu.api.receptacle.vanilla.window.ChestInventory
import trplugins.menu.api.receptacle.createReceptacle
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.module.internal.service.Performance
import trplugins.menu.util.Regexs
import trplugins.menu.util.collections.Variables
import kotlin.math.max

/**
 * @author Arasple
 * @date 2021/1/24 16:12
 */
class Layout(
    rows: Int = 0,
    val type: InventoryType,
    layout: List<String>,
    playerInventory: List<String>
) {

    private val width = width(type)

    val rows = when {
        type == InventoryType.CHEST -> max(rows, layout.size)
        type.defaultSize % 9 == 0 -> type.defaultSize / 9
        else -> 1
    }

    private val size = baseReceptacle().type.containerSize

    private val layout = mutableListOf<String>().also { while (it.size < rows) it.add(BLANK_LINE) }

    private val playerInventory = mutableListOf<String>().also { while (it.size < 4) it.add(BLANK_LINE) }

    val keys: Map<String, Set<Int>> by lazy {
        val keys = mutableMapOf<String, MutableSet<Int>>()

        layout.forEachIndexed { y, line ->
            listKeys(line).forEachIndexed { x, element ->
                val index = y * width + x
                keys.computeIfAbsent(element.toString()) { mutableSetOf() }.add(index)
            }

        }

        playerInventory.forEachIndexed { y, line ->
            listKeys(line).forEachIndexed { x, element ->
                val index = size + y * width + x - 1
                keys.computeIfAbsent(element.toString()) { mutableSetOf() }.add(index)
            }
        }

        keys
    }

    fun baseReceptacle() = type.createReceptacle().also {
        if (it is ChestInventory) it.rows = rows
    }

    fun initReceptacle(session: MenuSession) {
        val (_, menu, receptacle) = session.objects()
        menu ?: return
        receptacle ?: return

        receptacle.hidePlayerInventory(session.menu!!.settings.hidePlayerInventory)

        receptacle.onClose = { player, _ ->
            if (!Metadata.byBukkit(player, "FORCE_CLOSE")) {
                menu.settings.closeEvent.eval(adaptPlayer(session.viewer))
            }
            session.shut()
        }
        receptacle.onClick = onClick@{ player, event ->
            Performance.check("Menu:Event:Click") {
                val cancelEvent = {
                    event.isCancelled = true
                    event.refresh()
                }

                if (menu.settings.clickDelay.isCooldown(player.name)) {
                    return@onClick cancelEvent()
                } else if (!menu.isFreeSlot(event.slot)) {
                    cancelEvent()
                }

                submit(async = false) {
                    Performance.check("Menu:Event:ClickHandle") {
                        session.getIconProperty(event.slot)?.handleClick(event.receptacleClickType, session)
                    }
                }
            }
        }
    }

    fun isSimilar(layout: Layout): Boolean {
        return layout.rows == rows && layout.type == type
    }

    companion object {

        private const val BLANK_CHAR = " "
        private val BLANK_LINE = BLANK_CHAR.repeat(9)

        @Suppress("DEPRECATION")
        fun listKeys(line: String): List<Any> {
            return Variables(line, Regexs.ICON_KEY).element.flatMap {
                if (it.isVariable) listOf(it.value)
                else it.value.toList()
            }
        }

        private fun width(type: InventoryType): Int {
            return when (type.defaultSize) {
                27 -> 9
                5 -> 5
                else -> 3
            }
        }

    }

}