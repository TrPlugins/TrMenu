package cc.trixey.mc.invero

import cc.trixey.mc.invero.common.WindowHolder
import cc.trixey.mc.invero.common.event.BukkitInvEvent
import org.bukkit.event.inventory.*
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @since 2022/11/20 14:59
 */
object InveroListener {

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
            it.window.handleEvent(BukkitInvEvent(this))
        }
    }

}