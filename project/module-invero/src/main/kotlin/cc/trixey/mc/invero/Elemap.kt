package cc.trixey.mc.invero

/**
 * @author Arasple
 * @since 2022/11/21 23:19
 */
interface Elemap {

    val panel: cc.trixey.mc.invero.Panel

    /**
     * 是否存在某各个元素
     */
    fun has(element: cc.trixey.mc.invero.Element): Boolean

    /**
     * 添加某元素到指定槽位
     */
    fun add(slot: Int, element: cc.trixey.mc.invero.Element)

    fun add(vararg slots: Int, element: cc.trixey.mc.invero.Element) = slots.forEach { add(it, element) }

    fun add(slots: Set<Int>, element: cc.trixey.mc.invero.Element) = slots.forEach { add(it, element) }


    /**
     * 移除某元素
     */
    fun remove(element: cc.trixey.mc.invero.Element)

    /**
     * 移除某槽位的元素
     */
    fun remove(slot: Int)

    /**
     * 查询某元素占用的槽位
     */
    fun find(element: cc.trixey.mc.invero.Element): Set<Int>

    /**
     * 获得本元素集占用的所有槽位
     */
    fun getOccupiedSlots(): Set<Int>

    /**
     * 遍历元素
     */
    fun forEach(function: (cc.trixey.mc.invero.Element) -> Unit)

    /**
     * 遍历元素（带槽位）
     */
    fun forEachSloted(function: (element: cc.trixey.mc.invero.Element, slots: Set<Int>) -> Unit)

    operator fun get(slot: Int): cc.trixey.mc.invero.Element?

    operator fun set(slot: Int, element: cc.trixey.mc.invero.Element) = add(slot, element)

    operator fun minusAssign(element: cc.trixey.mc.invero.Element) = remove(element)

    fun clear()

    fun cc.trixey.mc.invero.Element.set(vararg slots: Int) = slots.forEach { add(it, this) }

    fun cc.trixey.mc.invero.Element.set(slots: Collection<Int>) = slots.forEach { add(it, this) }

}