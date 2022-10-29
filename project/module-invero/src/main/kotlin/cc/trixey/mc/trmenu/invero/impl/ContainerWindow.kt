package cc.trixey.mc.trmenu.invero.impl

import cc.trixey.mc.trmenu.invero.module.BaseWindow
import cc.trixey.mc.trmenu.invero.module.PairedInventory
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.util.handler
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 16:23
 */
open class ContainerWindow(
    viewer: UUID, title: String = "Untitled", override val type: TypeAddress = TypeAddress.ofRows(6)
) : BaseWindow(viewer) {

    override var title: String = title
        set(value) {
            getViewer()?.let {
                handler.updateWindowTitle(it, this, value)
            }
            field = value
        }

    override val inventory: PairedInventory by lazy {
        PairedInventory(Bukkit.createInventory(WindowHolder(this), type.bukkitType, title), null)
    }

    override fun handleClick(e: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

    override fun handleDrag(e: InventoryDragEvent) {
        TODO("Not yet implemented")
    }

    override fun handleItemsMove(e: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

    override fun handleItemsCollect(e: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

    override fun handleOpen(e: InventoryOpenEvent) {
        TODO("Not yet implemented")
    }

    override fun handleClose(e: InventoryCloseEvent) {
        TODO("Not yet implemented")
    }

}