package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.ElemapCompetent
import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.PanelScale
import cc.trixey.mc.invero.common.PanelWeight

/**
 * @author Arasple
 * @since 2022/11/6 16:47
 *
 * 基础的 Panel
 * 包含一个 ElemapCompetent（ Panel 元素集）
 */
abstract class BasePanel(
    scale: PanelScale, pos: Int, weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    /**
     * 元素
     */
    val elementsMap by lazy {
        ElemapCompetent(this)
    }

    /**
     * 向所有窗口渲染此 Panel
     */
    override fun renderPanel() {
        forWindows {
            elementsMap.forEach {
                renderElement(this, it)
            }
        }
    }

    /**
     * 取得占用的槽位
     */
    fun getOccupiedSlots(): Set<Int> {
        return elementsMap.getOccupiedSlots()
    }

    /**
     * 取得还未占用的槽位
     */
    fun getUnoccupiedSlots(): Set<Int> {
        return slots - getOccupiedSlots()
    }


    /**
     * 取得某槽位元素
     */
    operator fun get(slot: Int) = elementsMap[slot]

    /**
     * 设置某槽位元素
     */
    operator fun set(slot: Int, element: Element) = elementsMap.add(slot, element)

    /*
    拓展函数
     */

    fun Element.fillup(): Element {
        return set(getUnoccupiedSlots())
    }

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
            elementsMap += it
        }
    }

}