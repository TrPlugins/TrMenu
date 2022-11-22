package cc.trixey.mc.invero.common

import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.base.ElementDynamic
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/11/22 11:27
 *
 * 功能覆盖性元素集
 * 支持静态和动态槽位元素（ElementAbsolute、ElementDynamic）
 */
class ElemapCompetent : ElemapSimplified() {

    internal val dynamicElements = CopyOnWriteArrayList<ElementDynamic>()

    override fun has(element: Element): Boolean {
        return absoluteElements.containsValue(element) || dynamicElements.contains(element)
    }

    override fun remove(slot: Int) {
        absoluteElements.remove(slot)
    }

    override fun remove(element: Element) = find(element).forEach { remove(it) }

    fun add(element: ElementDynamic) {
        dynamicElements += element
    }

    override operator fun get(slot: Int) = absoluteElements[slot] ?: dynamicElements.find { it.slots.contains(slot) }

    override operator fun set(slot: Int, element: Element) {
        if (element is ElementAbsolute)
            absoluteElements[slot] = element
        else if (element is ElementDynamic)
            dynamicElements += element.also { it.slots(slot) }
    }

    override fun add(slot: Int, element: Element) = set(slot, element)

    override fun find(element: Element): Set<Int> {
        return if (element is ElementAbsolute) absoluteElements.filterValues { it == element }.keys
        else dynamicElements.find { it == element }?.slots ?: setOf()
    }

    override fun getOccupiedSlots() = absoluteElements.keys + dynamicElements.flatMap { it.slots }

    fun forEach(absolute: (ElementAbsolute) -> Unit, dynamic: (ElementDynamic) -> Unit) {
        absoluteElements.values.distinct().forEach(absolute)
        dynamicElements.forEach(dynamic)
    }

    override fun forEach(function: (Element) -> Unit) {
        absoluteElements.values.distinct().forEach(function)
        dynamicElements.forEach(function)
    }

    override fun forEachSloted(function: (element: Element, slots: Set<Int>) -> Unit) {
        absoluteElements.values.distinct().forEach { function(it, find(it)) }
        dynamicElements.forEach { function(it, find(it)) }
    }

    operator fun plusAssign(element: ElementDynamic) = add(element)

    override fun clear() {
        absoluteElements.clear()
        dynamicElements.clear()
    }

    /*
    拓展函数
     */
    fun ElementDynamic.add(): ElementDynamic {
        this@ElemapCompetent += this
        return this
    }

}