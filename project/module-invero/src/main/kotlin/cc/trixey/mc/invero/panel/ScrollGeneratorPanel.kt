package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.PanelScale
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.generator.GeneratorPanel
import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType

/**
 * @author Arasple
 * @since 2022/11/20 18:06
 *
 * 滚动生成器面板
 * 不支持设置静态物品，导航栏需要额外添加 Panel 实现
 */
class ScrollGeneratorPanel(
    scale: PanelScale,
    pos: Int,
    weight: PanelWeight,
    direction: ScrollDirection = ScrollDirection(vertical = true),
    type: ScrollType = ScrollType.STOP
) : ScrollStandardPanel(scale, pos, weight, direction, type), GeneratorPanel {

    /**
     * 最后一次生成的元素
     */
    override var lastGenerated: List<ElementAbsolute> = listOf()

    /**
     * 生成元素、裁切针对当前页码的有效元素集
     * 并设置元素
     */
    private fun apply() {
        colums.clear()
        val elements = lastGenerated
        elements.windowed(columCapacity, columCapacity, true).map { columElements ->
            colum {
                for (i in it) this[i] = columElements.getOrNull(i)
            }
        }
    }

    override fun <T : ElementAbsolute, R> generator(
        data: List<R>,
        spawner: (R) -> T,
        filter: ((T) -> Boolean)?,
        sorter: ((T) -> Int)?
    ) {
        super.generator(data, spawner, filter, sorter)
        apply()
    }

}