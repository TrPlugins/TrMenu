package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/6 16:11
 *
 * 映射槽位集
 *
 * input: <真实容器槽位> -> <相对槽位>
 * output: input.reversied
 */
class MappedSlots(val input: Map<Int, Int>, val output: Map<Int, Int>) {

    companion object {

        fun from(scale: Pair<Int, Int>, pos: Int, windowWidth: Int): MappedSlots {
            val result = mutableMapOf<Int, Int>()
            var counter = 0
            var baseLine = 0
            var baseIndex = pos
            while (baseIndex >= windowWidth) baseIndex -= windowWidth.also { baseLine++ }
            for (x in baseLine until baseLine + scale.second) for (y in baseIndex until baseIndex + scale.first) result[if (y >= windowWidth) -1 else windowWidth * x + y] =
                counter++

            return MappedSlots(result.filter { it.key >= 0 && it.value >= 0 })
        }

    }

    constructor(input: Map<Int, Int>) : this(input, input.map { it.value to it.key }.toMap())

    operator fun get(relative: Int) = getAbsolute(relative)

    fun getAbsolute(relative: Int) = output[relative] ?: error("Can not locate the absolute slot for [$relative]")

    fun getRelative(absolute: Int) = input[absolute] ?: error("Can not locate the relative slot for [$absolute]")

    val absoluteSlots by lazy {
        input.keys.toSet()
    }

}