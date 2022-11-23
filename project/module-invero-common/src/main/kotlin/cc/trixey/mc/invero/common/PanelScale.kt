package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/21 12:08
 */
@JvmInline
value class PanelScale(val pair: Pair<Int, Int>) {

    constructor(value: List<Int>) : this(value.first() to value.last())

    val width: Int
        get() = pair.first

    val height: Int
        get() = pair.second

    val area: Int
        get() = width * height

}