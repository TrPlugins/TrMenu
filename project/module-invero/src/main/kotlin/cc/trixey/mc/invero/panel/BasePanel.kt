package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.Element
import cc.trixey.mc.invero.PanelWeight
import cc.trixey.mc.invero.ScaleView
import cc.trixey.mc.invero.Window
import cc.trixey.mc.invero.item.ElemapCompetent
import cc.trixey.mc.invero.item.ElementDynamic
import cc.trixey.mc.invero.item.Interactable
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/6 16:47
 *
 * 基础的 Panel
 * 包含一个 ElemapCompetent（ Panel 元素集）
 */
abstract class BasePanel(scale: ScaleView, weight: PanelWeight) : PanelInstance(scale, weight) {

    /**
     * 元素
     */
    protected val elementsMap by lazy {
        ElemapCompetent(this)
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        super.handleClick(window, e)

        e.rawSlot.toLowerSlot()?.let {
            val element = elementsMap[it]
            if (element is Interactable) element.passClickEvent(e)
        }
    }

    override fun getRenderability(element: Element): Set<Int> {
        return elementsMap.findUpperSlots(this, element)
    }

    /**
     * 向所有窗口渲染此 Panel
     */
    override fun renderPanel() {
        forWindows {
            elementsMap.forEach { renderElement(this, it) }
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