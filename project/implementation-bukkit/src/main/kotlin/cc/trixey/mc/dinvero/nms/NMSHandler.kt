package cc.trixey.mc.dinvero.nms

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacketBlocking

/**
 * @author Arasple
 * @since 2022/10/20
 */
val handler by unsafeLazy {
    nmsProxy<NMS>()
}

internal fun Player.postPacket(packet: Any, vararg fields: Pair<String, Any?>): Any {
    packet.apply {
        fields.forEach { (key, value) ->
            setProperty(key, value)
        }
        sendPacketBlocking(this)

        println("Sent $name with a packet ${packet::class.java.name} / Size: ${packet.toString().length}")
        return this
    }
}

internal fun Player.sendWindowOpen(containerId: Int, type: WindowProperty, title: String) {
    handler.sendWindowOpen(this, containerId, type, title)
}

internal fun Player.sendWindowClose(containerId: Int) {
    handler.sendWindowClose(this, containerId)
}

internal fun Player.refreshWindowItems(containerId: Int, items: List<ItemStack?>) {
    handler.sendWindowItems(this, containerId, items)
}

internal fun Player.sendCancelCoursor() {
    sendWindowSetSlot(-1, -1, null, 1)
}

internal fun Player.sendWindowSetSlot(containerId: Int, slot: Int, itemStack: ItemStack?, stateId: Int = -1) {
    handler.sendWindowSetSlot(this, containerId, slot, itemStack, stateId)
}

fun Any.asCraftMirror(): ItemStack {
    return handler.asCraftMirror(this) as ItemStack
}