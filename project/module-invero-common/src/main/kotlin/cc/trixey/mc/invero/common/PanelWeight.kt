package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/1 21:43
 *
 * Panel 的权重，将决定渲染的顺序和处理交互的优先级
 */
enum class PanelWeight(val value: Int) {

    BACKGROUND(Int.MIN_VALUE),

    LOWEST(-10),

    LOW(-1),

    NORMAL(0),

    HIGH(1),

    HIGHEST(10),

    SURFACE(Int.MAX_VALUE)


}