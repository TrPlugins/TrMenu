package cc.trixey.mc.invero.common

import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.base.ElementDynamic
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/11/7 22:25
 */
class MappedElements : ElementsMap {

    /**
     * 静态（槽位不可变动）元素
     */
    internal val absoluteElements = ConcurrentHashMap<Int, ElementAbsolute>()

    /**
     * 动态元素
     */
    internal val dynamicElements = CopyOnWriteArrayList<ElementDynamic>()

    /**
     * 已占据的槽位
     */
    val occupiedSlots: Set<Int>
        get() = absoluteElements.keys + dynamicElements.flatMap { it.slots }

    /**
     * 判断是否有某个元素
     */
    override fun has(element: Element): Boolean {
        return absoluteElements.containsValue(element) || dynamicElements.contains(element)
    }

    /**
     * 移除某槽位的绝对元素
     */
    override fun remove(slot: Int) {
        // TODO Safe remove
        absoluteElements.remove(slot)
    }

    /**
     * 移除某元素
     */
    override fun remove(element: Element) {
        when (element) {
            is ElementAbsolute -> find(element).forEach { remove(it) }
            is ElementDynamic -> dynamicElements -= element
            else -> error("Unknown element type")
        }
    }

    /**
     * 添加元素
     */
    fun add(element: ElementDynamic) {
        dynamicElements += element
    }

    /**
     * 取得某槽位元素
     */
    override operator fun get(slot: Int) = absoluteElements[slot] ?: dynamicElements.find { it.slots.contains(slot) }

    override operator fun set(slot: Int, element: Element) {
        if (element is ElementAbsolute) {
            absoluteElements[slot] = element
        } else if (element is ElementDynamic) {
            dynamicElements += element.also { it.slots(slot) }
        }
    }

    /**
     * 设置某槽位元素
     */
    override fun add(slot: Int, element: Element) {
        when (element) {
            is ElementAbsolute -> absoluteElements[slot] = element
            is ElementDynamic -> dynamicElements += element.also { it.slots(slot) }
            else -> error("Unknown element type")
        }
    }

    /**
     * 搜索元素占有的槽位
     */
    override fun find(element: Element): Set<Int> {
        return if (element is ElementAbsolute) absoluteElements.filterValues { it == element }.keys
        else dynamicElements.find { it == element }?.slots ?: error("not found")
    }

    /**
     * 遍历元素
     */
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
        this@MappedElements += this
        return this
    }

}