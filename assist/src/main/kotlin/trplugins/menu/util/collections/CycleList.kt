package trplugins.menu.util.collections

/**
 * @author Arasple
 * @date 2021/1/23 23:15
 */
class CycleList<T>(val elements: List<T>) {

    constructor(vararg element: T) : this(element.toList())

    private val indexs = mutableMapOf<Int, Index>()

    operator fun get(index: Int): T {
        return elements[index]
    }

    fun isEmpty() = elements.isEmpty()

    fun next(id: Int): T? {
        return if (isEmpty()) null else this[cycleIndex(id)]
    }

    fun isFinal(id: Int): Boolean {
        return currentIndex(id) == elements.size - 1
    }

    fun current(id: Int): T? {
        return if (isEmpty()) null else this[currentIndex(id)]
    }

    private fun currentIndex(id: Int): Int {
        return if (isEmpty()) -1 else indexs.computeIfAbsent(id) { Index(elements.size - 1) }.current()
    }

    fun cycleIndex(id: Int): Int {
        return if (isEmpty()) -1 else indexs.computeIfAbsent(id) { Index(elements.size - 1) }.next()
    }

    fun cyclable(): Boolean {
        return elements.size > 1
    }

    fun reset(id: Int) {
        indexs.remove(id)
    }

    override fun toString(): String {
        return elements.joinToString(", ", "[", "]")
    }

    private class Index(private val limit: Int) {

        private var index: Int = -1

        fun current(): Int {
            return index.coerceAtLeast(0)
        }

        fun next(): Int {
            if (index < limit) index++
            else index = 0

            return index
        }

    }

}