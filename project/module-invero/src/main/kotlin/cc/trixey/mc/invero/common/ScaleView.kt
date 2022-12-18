package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/24 12:29
 */
class ScaleView(position: Int = -1, value: Pair<Int, Int>) : Scale {

    constructor(scale: Pair<Int, Int>) : this(value = scale)

    private var cache: Map<Int, Int>? = null
    private var cacheRevered: Map<Int, Int>? = null

    override var position: Int = position
        set(value) {
            resetCache()
            field = value
        }

    val pair: Pair<Int, Int>
        get() = width to height

    override val width: Int = value.first

    override val height: Int = value.second

    override val slots = (0 until area).toList()

    override fun getUpperSlot(upper: Scale, slot: Int): Int {
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
    override fun locate(slot: Int): Pair<Int, Int> {
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

    fun initCache(parent: Scalable) = initCache(parent.scale)

    fun initCache(scale: Scale) {
        cache = slots.associateWith { getUpperSlot(scale, it) }
        cacheRevered = cache!!.map { it.value to it.key }.toMap()
    }

    fun resetCache() {
        cache = null
    }

    override fun toString() = "$width/$height/$position"

    fun clone(): ScaleView {
        return ScaleView(position, pair)
    }

}