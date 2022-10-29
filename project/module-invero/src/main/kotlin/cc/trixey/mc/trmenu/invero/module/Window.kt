package cc.trixey.mc.trmenu.invero.module

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryEvent

/**
 * @author Arasple
 * @since 2022/10/29 10:55
 */
interface Window : Parentable {

    var title: String

    val panels: List<Panel>

    val type: TypeAddress

    val inventory: PairedInventory

    fun getViewer(): Player?

    fun handleEvent(e: InventoryEvent)

}