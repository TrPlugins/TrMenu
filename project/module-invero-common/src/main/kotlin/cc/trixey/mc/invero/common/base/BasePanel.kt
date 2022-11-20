package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.MappedElements
import cc.trixey.mc.invero.common.PanelWeight

/**
 * @author Arasple
 * @since 2022/11/6 16:47
 *
 * 基础的 Panel
 * 包含一个 MappedElements（ Panel 元素集）
 */
abstract class BasePanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    /**
     * 元素
     */
    private val elementsMap = MappedElements()

    /**
     * 向所有窗口渲染此 Panel
     */
    override fun renderPanel() {
        forWindows {
            elementsMap.forElements {
                renderElement(this, it)
            }
        }
    }

    /**
     * 取得占用的槽位
     */
    fun getOccupiedSlots(): Set<Int> {
        return elementsMap.occupiedSlots
    }

    /**
     * 取得还未占用的槽位
     */
    fun getUnoccupiedSlots(): Set<Int> {
        return slots - getOccupiedSlots()
    }

    /**
     * 取得元素集
     */
    fun getElementsMap(): MappedElements {
        return elementsMap
    }

    /**
     * 取得某槽位元素
     */
    operator fun get(slot: Int) = getElementsMap()[slot]

    /**
     * 设置某槽位元素
     */
    operator fun set(slot: Int, element: Element) = getElementsMap().add(slot, element)

    /*
    拓展函数
     */

    fun Element.set(slots: Set<Int>): Element {
        return this.also {
            slots.forEach { slot -> set(slot, it) }
        }
    }

    fun Element.set(vararg slots: Int): Element {
        return set(slots.toSet())
    }

    fun ElementDynamic.add(): ElementDynamic {
        return this.also {
            getElementsMap() += it
        }
    }

}