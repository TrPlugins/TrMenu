package cc.trixey.mc.trmenu.invero

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import org.bukkit.event.inventory.InventoryEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @since 2022/10/28 19:22
 */
object InveroManager {

    @SubscribeEvent
    fun e(e: InventoryEvent) {
        val holder = e.inventory.holder
        if (holder is WindowHolder) {
            holder.window.handleEvent(e)
        }
    }

}