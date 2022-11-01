package cc.trixey.mc.trmenu.invero

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import org.bukkit.event.inventory.*
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @since 2022/10/28 19:22
 */
object InveroManager {

    @SubscribeEvent
    fun e(e: InventoryClickEvent) = e.passInventoryEvent()

    @SubscribeEvent
    fun e(e: InventoryDragEvent) = e.passInventoryEvent()

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) = e.passInventoryEvent()

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) = e.passInventoryEvent()

    private fun InventoryEvent.passInventoryEvent() = inventory.holder.let {
        if (it is WindowHolder) {
            it.window.handleEvent(this)
        }
    }

}