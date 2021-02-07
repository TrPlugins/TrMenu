package me.arasple.mc.trmenu.module.display.layout

import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.api.receptacle.window.type.InventoryChest
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.service.Performance
import me.arasple.mc.trmenu.util.Regexs
import me.arasple.mc.trmenu.util.collections.Variables
import org.bukkit.event.inventory.InventoryType
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

    private val rows = when {
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
                val index = size + y * width + x
                keys.computeIfAbsent(element.toString()) { mutableSetOf() }.add(index)
            }
        }

        keys
    }

    fun baseReceptacle() = ReceptacleAPI.createReceptacle(type).also {
        if (it is InventoryChest) it.rows = rows
    }

    fun initReceptacle(session: MenuSession) {
        val (_, menu, receptacle) = session.objects()
        menu ?: return
        receptacle ?: return

        receptacle.listenerClose { _, _ ->
            menu.settings.closeEvent.eval(session)
            session.shut()
        }
        receptacle.listenerClick { player, event ->
            Performance.MIRROR.check("Menu:Event:handleClick") {
                val cancelEvent = {
                    event.isCancelled = true
                    receptacle.refresh(event.slot)
                    if (event.clickType.isItemMoveable()) {
                        event.receptacle.type.hotBarSlots.forEach(receptacle::refresh)
                        event.receptacle.type.mainInvSlots.forEach(receptacle::refresh)
                    }
                }

                if (menu.settings.clickDelay.isCooldown(player.name)) {
                    return@listenerClick cancelEvent()
                } else if (!menu.isFreeSlot(event.slot)) {
                    cancelEvent()
                }

                session.getIconProperty(event.slot)?.handleClick(event.clickType, session)
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