package trplugins.menu.api.receptacle

import org.bukkit.entity.Player

/**
 * TrMenu
 * trplugins.menu.api.receptacle.Receptacle
 *
 * @author Score2
 * @since 2022/03/10 19:28
 */
abstract class Receptacle<Element>(val layout: ReceptacleLayout) {

    abstract var title: String
    abstract var onOpen: ((player: Player, receptacle: Receptacle<Element>) -> Unit)
    abstract var onClose: ((player: Player, receptacle: Receptacle<Element>) -> Unit)
    abstract var onClick: ((player: Player, event: ReceptacleInteractEvent<Element>) -> Unit)

    abstract fun getElement(slot: Int): Element?
    abstract fun hasElement(slot: Int): Boolean
    abstract fun setElement(element: Element? = null, vararg slots: Int, display: Boolean = true)

    abstract fun clear(display: Boolean = true)
    abstract fun refresh(slot: Int = -1)
    abstract fun open(player: Player)
    abstract fun close(sendPacket: Boolean = true)
    abstract fun callEventClick(event: ReceptacleInteractEvent<Element>)


    fun setElement(element: Element?, slots: Collection<Int>, display: Boolean = true) {
        setElement(element, *slots.toIntArray(), display = display)
    }

    fun onOpen(handler: (player: Player, receptacle: Receptacle<Element>) -> Unit) {
        this.onOpen = handler
    }

    fun onClose(handler: (player: Player, receptacle: Receptacle<Element>) -> Unit) {
        this.onClose = handler
    }

    fun onClick(clickEvent: (player: Player, event: ReceptacleInteractEvent<Element>) -> Unit) {
        this.onClick = clickEvent
    }
}