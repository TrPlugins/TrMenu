package cc.trixey.mc.invero.impl.container.panel

import cc.trixey.mc.invero.PanelWeight
import cc.trixey.mc.invero.ScaleView
import cc.trixey.mc.invero.item.ElementAbsolute
import cc.trixey.mc.invero.panel.generator.GeneratorPanel
import cc.trixey.mc.invero.panel.scroll.ScrollDirection
import cc.trixey.mc.invero.panel.scroll.ScrollType

/**
 * @author Arasple
 * @since 2022/11/20 18:06
 *
 * 滚动生成器面板
 * 栏目仅接受静态元素
 */
class ScrollGeneratorPanel(
    scale: ScaleView,
    weight: PanelWeight,
    direction: ScrollDirection = ScrollDirection(vertical = true),
    type: ScrollType = ScrollType.STOP
) : ScrollStandardPanel(scale, weight, direction, type), GeneratorPanel {

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
            addColum {
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