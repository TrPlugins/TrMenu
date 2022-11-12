package cc.trixey.mc.trmenu.invero.module.element

import cc.trixey.mc.trmenu.invero.module.Panel
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/12 20:12
 */
abstract class Interactable(override val panel: Panel) : PanelElement {

    private var handlerClickEvent: (InventoryClickEvent) -> Unit = {}

    fun onClick(event: InventoryClickEvent.() -> Unit) {
        handlerClickEvent = event
    }

    fun passClickEvent(e: InventoryClickEvent) = handlerClickEvent(e)

}