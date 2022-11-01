package cc.trixey.mc.trmenu.invero.impl

import cc.trixey.mc.trmenu.invero.event.PanelRenderEvent
import cc.trixey.mc.trmenu.invero.module.IconElement
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
class BasePanel {

    /**
     * Claimed Slots
     * Not necessarily need to render it
     */
    val claimed: CopyOnWriteArrayList<Int> = CopyOnWriteArrayList()

    /**
     * Panel Elements
     */
    val elements: LinkedList<IconElement> = LinkedList()

    internal var eventRender: ((event: PanelRenderEvent) -> Unit) = {}

    fun onRender(e: (event: PanelRenderEvent) -> Unit) {
        eventRender = e
    }

}