package cc.trixey.mc.trmenu.invero.struc

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

/**
 * @author Arasple
 * @since 2022/10/29 10:55
 */
interface Window : Parentable {

    var title: String

    val panels: List<Panel>

    val type: InventoryType

    val inventories: Array<Inventory>

    fun getViewer(): Player?

    fun handleEvent(e: InventoryEvent)

}