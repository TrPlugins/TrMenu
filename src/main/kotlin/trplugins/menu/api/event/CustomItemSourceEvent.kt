package trplugins.menu.api.event

import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

class CustomItemSourceEvent(val name: String, val id: String, var source: ItemStack? = null) : BukkitProxyEvent() {

    override val allowCancelled: Boolean
        get() = false
}