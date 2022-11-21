package cc.trixey.mc.invero.common.generator

/**
 * @author Arasple
 * @since 2022/11/13 15:58
 */
class Generator<T>(val capacity: Int, val generator: (Int) -> T) {

    private lateinit var last: List<T>

    fun lastGenerated(): List<T> {
        return last
    }

    fun spawn(): List<T> {
        return buildList {
            for (index in 0 until capacity) {
                add(index, generator(index))
            }
        }.also { last = it }
    }

}