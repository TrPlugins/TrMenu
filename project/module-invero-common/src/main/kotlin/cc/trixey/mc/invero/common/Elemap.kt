package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/21 23:19
 */
interface Elemap {

    /**
     * 是否存在某各个元素
     */
    fun has(element: Element): Boolean

    /**
     * 添加某元素到指定槽位
     */
    fun add(slot: Int, element: Element)

    fun add(vararg slots: Int, element: Element) = slots.forEach { add(it, element) }

    fun add(slots: Set<Int>, element: Element) = slots.forEach { add(it, element) }


    /**
     * 移除某元素
     */
    fun remove(element: Element)

    /**
     * 移除某槽位的元素
     */
    fun remove(slot: Int)

    /**
     * 查询某元素占用的槽位
     */
    fun find(element: Element): Set<Int>

    /**
     * 获得本元素集占用的所有槽位
     */
    fun getOccupiedSlots(): Set<Int>

    /**
     * 遍历元素
     */
    fun forEach(function: (Element) -> Unit)

    /**
     * 遍历元素（带槽位）
     */
    fun forEachSloted(function: (element: Element, slots: Set<Int>) -> Unit)

    operator fun get(slot: Int): Element?

    operator fun set(slot: Int, element: Element) = add(slot, element)

    operator fun minusAssign(element: Element) = remove(element)

    fun clear()

    fun Element.set(vararg slots: Int) = slots.forEach { add(it, this) }

    fun Element.set(slots: Collection<Int>) = slots.forEach { add(it, this) }

}