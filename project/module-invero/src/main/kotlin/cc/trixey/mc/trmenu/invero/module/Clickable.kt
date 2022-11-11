package cc.trixey.mc.trmenu.invero.module

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/8 21:43
 */
interface Clickable {

    var handler: (e: InventoryClickEvent) -> Unit

    fun onClick(value: InventoryClickEvent.() -> Unit) {
        handler = value
    }

    fun handleClick(e: InventoryClickEvent) {
        handler(e)
    }

}