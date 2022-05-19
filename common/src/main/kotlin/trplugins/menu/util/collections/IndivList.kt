package trplugins.menu.util.collections

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
        // 缓解燃眉之急
        return try {
            if (isEmpty()) null else elements[getIndex(id)]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun getIndex(id: Int): Int {
        return if (isEmpty()) -1 else indexs.computeIfAbsent(id) { -1 }
    }


}