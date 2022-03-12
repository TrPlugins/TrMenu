package trplugins.menu.api.receptacle

import org.bukkit.entity.Player

/**
 * TrMenu
 * trplugins.menu.api.receptacle.Receptacle
 *
 * @author Score2
 * @since 2022/03/10 19:28
 */
abstract class Receptacle<T>(val layout: ReceptacleLayout) {

    abstract var title: String
    abstract var onOpen: ((player: Player, receptacle: Receptacle<T>) -> Unit)
    abstract var onClose: ((player: Player, receptacle: Receptacle<T>) -> Unit)
    abstract var onClick: ((player: Player, event: ReceptacleInteractEvent) -> Unit)

    abstract fun getElement(slot: Int): Element<T>?
    abstract fun hasElement(slot: Int): Boolean
    abstract fun setElement(element: Element<T>? = null, vararg slots: Int, display: Boolean = true)

    abstract fun clear(display: Boolean = true)
    abstract fun refresh(slot: Int = -1)
    abstract fun open(player: Player)
    abstract fun close(sendPacket: Boolean = true)
    abstract fun callEventClick(event: ReceptacleInteractEvent)


    fun setElement(element: Element<T>?, slots: Collection<Int>, display: Boolean) {
        setElement(element, *slots.toIntArray(), display = display)
    }

    fun onOpen(handler: (player: Player, receptacle: Receptacle<T>) -> Unit) {
        this.onOpen = handler
    }

    fun onClose(handler: (player: Player, receptacle: Receptacle<T>) -> Unit) {
        this.onClose = handler
    }

    fun onClick(clickEvent: (player: Player, event: ReceptacleInteractEvent) -> Unit) {
        this.onClick = clickEvent
    }
}