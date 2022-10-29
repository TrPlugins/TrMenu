package cc.trixey.mc.trmenu.invero.util

import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacketBlocking

/**
 * @author Arasple
 * @since 2022/10/29 16:47
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
        return this
    }
}