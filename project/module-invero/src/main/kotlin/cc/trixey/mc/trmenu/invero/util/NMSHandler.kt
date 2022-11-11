package cc.trixey.mc.trmenu.invero.util

import org.bukkit.entity.Player
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacketBlocking

/**
 * @author Arasple
 * @since 2022/10/29 16:47
 */

/**
 * NMS Handler instance of this module
 *
 * @see NMSImpl
 */
val handler = nmsProxy<NMS>()

/**
 * Send a packet modified by custom fileds to a player
 */
internal fun Player.postPacket(packet: Any, vararg fields: Pair<String, Any?>): Any {
    packet.apply {
        fields.forEach { (key, value) ->
            setProperty(key, value)
        }
        sendPacketBlocking(this)
        return this
    }
}