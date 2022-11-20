package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.Panel
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/12 20:12
 */
abstract class Interactable(override val panel: Panel) : Element {

    private var handlerClickEvent: (InventoryClickEvent) -> Unit = {}

    fun onClick(event: InventoryClickEvent.() -> Unit) {
        handlerClickEvent = event
    }

    fun passClickEvent(e: InventoryClickEvent) = handlerClickEvent(e)

}