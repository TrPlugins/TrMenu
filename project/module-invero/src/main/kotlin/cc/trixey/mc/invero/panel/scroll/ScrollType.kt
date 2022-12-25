package cc.trixey.mc.invero.panel.scroll

/**
 * @author Arasple
 * @since 2022/11/18 17:53
 */
@JvmInline
value class ScrollType(val value: Int = 0) {

    val name: String
        get() = when {
            isStop -> "STOP"
            isLoop -> "LOOP"
            else -> error("")
        }

    /**
     * 截止滚动
     *
     * 当滚动到还剩 value 个栏目时停止
     */
    val isStop: Boolean
        get() = value >= 0

    /**
     * 循环滚动
     *
     * 当滚动到最后一个栏目后继续从首个栏目获取元素
     */
    val isLoop: Boolean
        get() = value < 0

    fun stopAt(value: Int): ScrollType {
        return if (this.value > 0 && value > 0) ScrollType(value)
        else this
    }

    companion object {

        fun random(): ScrollType {
            return arrayOf(STOP, LOOP).random()
        }

        fun stopAt(value: Int): ScrollType {
            return ScrollType(value.coerceAtLeast(0))
        }

        val STOP = ScrollType(0)

        val LOOP = ScrollType(-1)

    }

}
