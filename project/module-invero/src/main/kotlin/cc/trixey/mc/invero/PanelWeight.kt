package cc.trixey.mc.invero

/**
 * @author Arasple
 * @since 2022/11/1 21:43
 *
 * Panel 的权重，将决定渲染的顺序和处理交互的优先级
 */
@JvmInline
value class PanelWeight(private val value: Int) : Comparable<PanelWeight> {

    companion object {

        val BACKGROUND = PanelWeight(Int.MIN_VALUE)

        val LOWEST = PanelWeight(-10)

        val LOW = PanelWeight(-1)

        val NORMAL = PanelWeight(0)

        val HIGH = PanelWeight(1)

        val HIGHEST = PanelWeight(10)

        val SURFACE = PanelWeight(Int.MAX_VALUE)

    }

    override fun compareTo(other: PanelWeight): Int {
        return value.compareTo(other.value)
    }


}