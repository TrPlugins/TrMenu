package cc.trixey.mc.trmenu.test

/**
 * @author Arasple
 * @since 2022/11/24 16:33
 */

/**
 * @author Arasple
 * @since 2022/11/24 12:29
 */
class ScaleView(position: Int = -1, value: Pair<Int, Int>) {

    constructor(scale: Pair<Int, Int>) : this(value = scale)

    private var cache: Map<Int, Int>? = null
    private var cacheRevered: Map<Int, Int>? = null

    var position: Int = position
        set(value) {
            resetCache()
            field = value
        }

    val pair: Pair<Int, Int>
        get() = width to height

    val width: Int = value.first

    val height: Int = value.second

    val slots = (0 until width * height).toList()

    fun getUpperSlot(upper: ScaleView, slot: Int): Int {
        val (x, y) = locate(slot)
        val (pX, pY) = upper.locate(position)
        val (rX, rY) = pX + x to pY + y
        if (rX >= upper.width) return -1
        return rY * upper.width + rX
    }

    /**
     * x = 列 - 1
     * y = 行 - 1
     */
    fun locate(slot: Int): Pair<Int, Int> {
        val y = slot / width
        val x = slot - y * width
        return x to y
    }

    fun getCache(): Map<Int, Int>? {
        return cache
    }

    fun getCacheReversed(): Map<Int, Int>? {
        return cacheRevered
    }

    fun resetCache() {
        cache = null
    }

    override fun toString() = "$width/$height/$position"

    fun clone(): ScaleView {
        return ScaleView(position, pair)
    }

}

fun main() {
    val freeform = ScaleView(0, 9 to 6)
    val inner = ScaleView(7, 3 to 3)

    // 7, 8
    // 16, 17


    for (i in 0..8) {
        println(inner.getUpperSlot(freeform, i))
    }
}