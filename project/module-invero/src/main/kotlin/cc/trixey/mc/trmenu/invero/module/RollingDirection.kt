package cc.trixey.mc.trmenu.invero.module

/**
 * @author Arasple
 * @since 2022/11/17 11:21
 */
@JvmInline
value class RollingDirection(private val vertical: Boolean = true) {

    val isHorizontal: Boolean
        get() = !vertical

    val isVertical: Boolean
        get() = vertical

}