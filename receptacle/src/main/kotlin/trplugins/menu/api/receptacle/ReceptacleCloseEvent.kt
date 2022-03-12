package trplugins.menu.api.receptacle

import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent

/**
 * @author Arasple
 * @date 2020/12/5 21:42
 */
class ReceptacleCloseEvent<Element>(val player: Player, val receptacle: Receptacle<Element>) : ProxyEvent() {

    override val allowCancelled: Boolean
        get() = false
}