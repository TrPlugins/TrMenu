package cc.trixey.mc.dinvero.event

import cc.trixey.mc.dinvero.Invero
import cc.trixey.mc.dinvero.nms.WindowProperty
import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent

/**
 * @author Arasple
 * @since 2022/10/22
 */
open class InveroEvent(
    val player: Player,
    val invero: Invero,
) : ProxyEvent() {

    val property: WindowProperty
        get() = invero.property

    fun release() {
        invero.release()
    }

}