package cc.trixey.mc.invero.common.generator

/**
 * @author Arasple
 * @since 2022/11/13 15:58
 */
class Generator<T, R>(
    val data: List<R>,
    val generator: (R) -> T,
    var filter: ((T) -> Boolean)? = null,
    var sorter: ((T) -> Int?)? = null
) {

    fun spawn(): List<T> {
        val raw = buildList {
            for (i in data.indices) {
                add(i, generator(data[i]))
            }
        }
        if (filter != null) raw.filter(filter!!)
        if (sorter != null) raw.sortedBy(sorter!!)
        return raw
    }

}