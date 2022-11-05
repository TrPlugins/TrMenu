package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 10:55
 */
interface Window : Parentable {

    var title: String

    val panels: LinkedList<Panel>

    val type: TypeAddress

    val pairedInventory: PairedInventory

    fun getViewerSafe(): Player?

    fun open()

    fun handleEvent(e: InventoryEvent)

    fun hasViewer(): Boolean {
        return getViewerSafe()?.let {
            val holder = it.openInventory.topInventory.holder
            holder is WindowHolder && holder.window == this
        } ?: false
    }

}