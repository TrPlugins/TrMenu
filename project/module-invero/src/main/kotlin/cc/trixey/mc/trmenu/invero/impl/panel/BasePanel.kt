package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.*
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
class BasePanel(
    override val window: Window,
    override val size: Pair<Int, Int>,
    override val posMark: Int,
    override var weight: PanelWeight = PanelWeight.NORMAL
) : Panel {

    override val elements: LinkedHashMap<Int, PanelElement> = LinkedHashMap()
    override val dynamicElements: LinkedList<PanelElementDynamic> = LinkedList()

    /**
     * Claimed Slots Map
     * (Relative: Actual)
     */
    override val slotsMap = run {
        // Relative: Absolute
        val result = LinkedHashMap<Int, Int>()
        var initial = posMark
        val (width, height) = size

        for (line in 0 until height) {
            for (row in 0 until width) {
                result[line * height + line + row] = initial
                initial++
            }
            initial += (window.type.width - size.first)
        }
        result
    }

    /**
     * No children panels supported
     */
    override fun getChildren() = null

}