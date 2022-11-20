package cc.trixey.mc.invero.common.scroll

/**
 * @author Arasple
 * @since 2022/11/17 11:21
 */
@JvmInline
value class ScrollDirection(private val vertical: Boolean = true) {

    /**
     * 是否为垂直滚动
     * （默认）
     *
     * 栏目元素将作为 <行> 来上下滚动
     */
    val isVertical: Boolean
        get() = vertical

    /**
     * 是否为水平滚动
     *
     * 栏目元素将作为 <列> 来左右滚动
     */
    val isHorizontal: Boolean
        get() = !vertical

}