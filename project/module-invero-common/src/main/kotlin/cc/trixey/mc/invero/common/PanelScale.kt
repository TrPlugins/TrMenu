package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/21 12:08
 */
@JvmInline
value class PanelScale(private val value: Pair<Int, Int>) {

    constructor(value: List<Int>) : this(value.first() to value.last())

    val width: Int
        get() = value.first

    val height: Int
        get() = value.second

    val area: Int
        get() = width * height

}