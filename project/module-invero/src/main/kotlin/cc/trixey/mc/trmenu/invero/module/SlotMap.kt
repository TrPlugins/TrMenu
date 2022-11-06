package cc.trixey.mc.trmenu.invero.module

/**
 * @author Arasple
 * @since 2022/11/6 16:11
 *
 * (Default) Input: Actual -> Relative
 * Output: Relative -> Actual
 *
 */
data class SlotMap(val input: Map<Int, Int>, val output: Map<Int, Int>) {

    operator fun get(relative: Int) = getActual(relative)

    fun getActual(relative: Int) = output[relative] ?: -1

    fun getRelative(actual: Int) = input[actual] ?: -1


    val claimedSlots: List<Int> by lazy {
        input.keys.toList()
    }

    constructor(input: Map<Int, Int>) : this(input, input.map { it.value to it.key }.toMap())

}