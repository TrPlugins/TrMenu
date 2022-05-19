package trplugins.menu.api.event

import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent
import trplugins.menu.module.display.MenuSession

class CustomItemSourceEvent(
    val name: String,
    val id: String,
    val session: MenuSession,
    var source: ItemStack? = null
) :
    BukkitProxyEvent() {

    override val allowCancelled: Boolean
        get() = false
}