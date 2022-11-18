package cc.trixey.mc.trmenu.invero.module

/**
 * @author Arasple
 * @since 2022/11/6 16:11
 *
 * (Default) Input: Actual -> Relative
 * Output: Relative -> Actual
 *
 */
class MappedSlots(val input: Map<Int, Int>, val output: Map<Int, Int>) {

    companion object {

        fun from(scale: Pair<Int, Int>, pos: Int, windowWidth: Int): MappedSlots {
            val result = mutableMapOf<Int, Int>()
            var counter = 0
            var baseLine = 0
            var baseIndex = pos
            while (baseIndex >= windowWidth) baseIndex -= windowWidth.also { baseLine++ }
            for (x in baseLine until baseLine + scale.second)
                for (y in baseIndex until baseIndex + scale.first)
                    result[if (y >= windowWidth) -1 else windowWidth * x + y] = counter++

            return MappedSlots(result.filter { it.key >= 0 && it.value >= 0 })
        }

    }

    constructor(input: Map<Int, Int>) : this(input, input.map { it.value to it.key }.toMap())

    operator fun get(relative: Int) = getActual(relative)

    fun getActual(relative: Int) = output[relative] ?: -1

    fun getRelative(actual: Int) = input[actual] ?: -1

    /**
     * Actual slots that this panel have claimed
     */
    val claimedSlots by lazy {
        input.keys.toSet()
    }

}