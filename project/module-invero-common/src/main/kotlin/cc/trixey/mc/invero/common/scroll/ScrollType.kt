package cc.trixey.mc.invero.common.scroll

/**
 * @author Arasple
 * @since 2022/11/18 17:53
 */
enum class ScrollType(val value: Int = 1) {

    /**
     * 截止滚动
     *
     * 当滚动到最后一个栏目的元素时不再支持继续滚动
     */
    STOP,

    /**
     * 循环滚动
     *
     * 当滚动到最后一个栏目后继续从首个栏目获取元素
     */
    LOOP,

    /**
     * 留白滚动
     *
     * 当滚动到最后一个栏目后继续滚动将产生空白区域，直到只剩 $value 行栏目可视
     */
    BLANK

}
