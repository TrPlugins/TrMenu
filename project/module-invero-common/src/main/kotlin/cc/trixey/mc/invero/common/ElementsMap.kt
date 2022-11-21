package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/21 23:19
 */
interface ElementsMap {

    fun has(element: Element): Boolean

    fun add(slot: Int, element: Element)

    fun add(vararg slots: Int, element: Element) = slots.forEach { add(it, element) }

    fun add(slots: Set<Int>, element: Element) = slots.forEach { add(it, element) }

    fun remove(element: Element)

    fun remove(slot: Int)

    fun find(element: Element): Set<Int>

    fun forEach(function: (Element) -> Unit)

    fun forEachSloted(function: (element: Element, slots: Set<Int>) -> Unit)

    operator fun get(slot: Int): Element?

    operator fun set(slot: Int, element: Element) = add(slot, element)

    operator fun minusAssign(element: Element) = remove(element)

    fun clear()

    fun Element.set(vararg slots: Int) = slots.forEach { add(it, this) }

}