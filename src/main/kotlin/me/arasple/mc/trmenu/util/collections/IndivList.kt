package me.arasple.mc.trmenu.util.collections

/**
 * @author Arasple
 * @date 2021/1/23 23:15
 */
class IndivList<T>(val elements: List<T>) {

    init {
        elements.sortedBy { it.hashCode() }
    }

    private val indexs = mutableMapOf<Int, Int>()

    fun isEmpty() = elements.isEmpty()

    operator fun set(id: Int, value: Int) {
        indexs[id] = value
    }

    operator fun get(id: Int): T? {
        return if (isEmpty()) null else elements[getIndex(id)]
    }

    fun getIndex(id: Int): Int {
        return if (isEmpty()) -1 else indexs.computeIfAbsent(id) { -1 }
    }


}