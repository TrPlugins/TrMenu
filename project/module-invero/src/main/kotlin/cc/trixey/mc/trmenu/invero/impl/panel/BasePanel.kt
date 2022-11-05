package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.*
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
class BasePanel(
    scale: Pair<Int, Int>, pos: Int, override var weight: PanelWeight = PanelWeight.NORMAL
) : Panel {

    override var scale = scale
        set(value) {
            field = value
            slotsMap.clear()
        }

    override val slots: List<Int> = (scale.first to scale.second).toList()

    override var pos = pos
        set(value) {
            field = value
            slotsMap.clear()
        }

    override val elements: LinkedHashMap<Int, PanelElement> = LinkedHashMap()
    override val dynamicElements: LinkedList<PanelElementDynamic> = LinkedList()

    /**
     * Claimed Slots Map
     * (Relative: Actual)
     *
     */
    private val slotsMap = LinkedHashMap<Int, Map<Int, Int>>()

    override fun getSlotsMap(window: Window) = getSlotsMap(window.type.width)

    fun getSlotsMap(windowWidth: Int): Map<Int, Int> {
        return slotsMap.computeIfAbsent(windowWidth) {
            val result = LinkedHashMap<Int, Int>()
            var initial = pos
            val (width, height) = scale

            for (line in 0 until height) {
                for (row in 0 until width) {
                    result[line * height + line + row] = initial
                    initial++
                }
                initial += (width - scale.first)
            }

            result
        }
    }

    /**
     * No children panels supported
     */
    override fun getChildren() = null

    override fun getParent() = null

}