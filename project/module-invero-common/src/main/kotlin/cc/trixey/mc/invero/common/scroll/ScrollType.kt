package cc.trixey.mc.invero.common.scroll

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
            isBlank -> "BLANK"
            else -> error("")
        }

    /**
     * 截止滚动
     *
     * 当滚动到最后一个栏目的元素时不再支持继续滚动
     */
    val isStop: Boolean
        get() = value == 0

    /**
     * 循环滚动
     *
     * 当滚动到最后一个栏目后继续从首个栏目获取元素
     */
    val isLoop: Boolean
        get() = value < 0

    /**
     * 留白滚动
     *
     * 当滚动到最后一个栏目后继续滚动将产生空白区域，直到只剩 $value 行栏目可视
     */
    val isBlank: Boolean
        get() = value > 0

    fun blankBy(value: Int): ScrollType {
        return if (this.value > 0 && value > 0) ScrollType(value)
        else this
    }

    companion object {

        fun random(): ScrollType {
            return arrayOf(STOP, LOOP, BLANK).random()
        }

        val STOP = ScrollType()

        val LOOP = ScrollType(-1)

        val BLANK = ScrollType(1)

    }

}
