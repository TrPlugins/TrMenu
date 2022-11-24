package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/24 12:29
 */
class ScaleView(override val position: Int = -1, value: Pair<Int, Int>) : Scale {

    constructor(scale: Pair<Int, Int>) : this(value = scale)

    private var cache: Map<Int, Int>? = null
    private var cacheRevered: Map<Int, Int>? = null

    val pair: Pair<Int, Int>
        get() = width to height

    override val width: Int = value.first

    override val height: Int = value.second

    override val slots = (0 until area).toList()

    override fun getUpperSlot(upper: Scale, slot: Int): Int {
        val (x, y) = locate(slot)
        val (pX, pY) = upper.locate(position)
        val (rX, rY) = pX + x to pY + y

        return rX * upper.width + rY
    }

    override fun locate(slot: Int): Pair<Int, Int> {
        val x = slot / width
        val y = slot - x * width
        return x to y
    }

    fun getCache(): Map<Int, Int>? {
        return cache
    }

    fun getCacheReversed(): Map<Int, Int>? {
        return cacheRevered
    }

    fun initCache(parent: Scalable) {
        if (isWindow()) error("Window can not set parentCache for scaled slots")
        cache = slots.associateWith { getUpperSlot(parent.scale, it) }
        cacheRevered = cache!!.map { it.value to it.key }.toMap()
    }

    fun isWindow(): Boolean {
        return position < 0
    }

    fun resetCache() {
        cache = null
    }

    override fun toString() = "$width/$height/$position"

}