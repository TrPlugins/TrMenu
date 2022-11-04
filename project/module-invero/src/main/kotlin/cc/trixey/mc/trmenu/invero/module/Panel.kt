package cc.trixey.mc.trmenu.invero.module

import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 10:59
 */
interface Panel : Parentable {

    /**
     * The window to which this panel belongs
     */
    val window: Window

    /**
     * Panel weight
     * Weight can define the render priority of the panel's elements
     */
    var weight: PanelWeight

    /**
     * Panel Elements
     */
    val elements: LinkedList<PanelElement>

    override fun getParent() = window

    fun weight(weight: PanelWeight) {
        this.weight = weight
    }

    fun render()

}