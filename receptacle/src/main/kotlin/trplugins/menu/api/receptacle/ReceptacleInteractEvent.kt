package trplugins.menu.api.receptacle

import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent

/**
 * @author Arasple
 * @date 2020/12/5 21:42
 */
class ReceptacleInteractEvent<Element>(val player: Player, val receptacle: Receptacle<Element>, val receptacleClickType: ReceptacleClickType, val slot: Int) : ProxyEvent() {

    var element: Element?
        set(value) = receptacle.setElement(value, slot)
        get() = receptacle.getElement(slot)

    fun refresh() {
        if (receptacleClickType.isItemMoveable()) {
            receptacle.layout.hotBarSlots.forEach { receptacle.refresh(it) }
            receptacle.layout.mainInvSlots.forEach { receptacle.refresh(it) }
            receptacle.layout.containerSlots.forEach { receptacle.refresh(it) }
        } else {
            receptacle.refresh(slot)
        }
    }
}