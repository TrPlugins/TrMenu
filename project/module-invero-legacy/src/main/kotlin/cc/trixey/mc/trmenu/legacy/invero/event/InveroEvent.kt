package cc.trixey.mc.trmenu.legacy.invero.event

import cc.trixey.mc.trmenu.legacy.invero.nms.WindowProperty
import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent

/**
 * @author Arasple
 * @since 2022/10/22
 */
open class InveroEvent(
    val player: Player,
    val invero: cc.trixey.mc.trmenu.legacy.invero.Invero,
) : ProxyEvent() {

    val property: WindowProperty
        get() = invero.property

    fun release() {
        invero.release()
    }

}