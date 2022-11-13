package cc.trixey.mc.trmenu.invero.module

/**
 * @author Arasple
 * @since 2022/11/13 15:58
 */
class Generator<T>(val capacity: Int, val generator: (Int) -> T) {

    fun generate(): List<T> {
        return buildList {
            for (index in 0 until capacity) {
                add(index, generator(index))
            }
        }
    }

}