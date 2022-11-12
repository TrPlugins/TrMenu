package cc.trixey.mc.trmenu.invero.module

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/6 15:17
 */
abstract class PanelElementAbsolute(override val panel: Panel) : PanelElement {

    private var handlerClickEvent: (InventoryClickEvent) -> Unit = {}
    private val absoltueSlots = LinkedHashMap<Int, List<Int>>()

    val slots by lazy {
        panelElements.findElementSlots(this).ifEmpty { null }
    }

    fun onClick(event: InventoryClickEvent.() -> Unit) {
        handlerClickEvent = event
    }

    fun passClickEvent(e: InventoryClickEvent) = handlerClickEvent(e)

    fun Window.getAbsoluteSLots(): List<Int> {
        return absoltueSlots.computeIfAbsent(type.width) {
            slots!!.map { slotMap().getActual(it) }.filter { it >= 0 }
        }
    }

}