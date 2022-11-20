package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.MappedElements
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.generator.Generator

/**
 * @author Arasple
 * @since 2022/11/13 15:33
 *
 * 逻辑上只使用 (PagedStandardPanel) 的一页元素 pagedElements[0]
 * 动态生成元素后再进行设置
 *
 * staticElements 作为强制覆盖元素，其占用槽位自动被排除生成池槽位
 */
class PagedGeneratorPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight,
    var exclude: Set<Int> = setOf()
) : PagedStandardPanel(scale, pos, weight) {

    lateinit var generator: Generator<ElementAbsolute>
    override var maxPageIndex = 0
    private val pool
        get() = slots - staticElements.occupiedSlots - exclude

    private var lastGenerated: List<ElementAbsolute> = listOf()
        set(value) {
            maxPageIndex = value.size / pool.size
            if (pagedElements.lastIndex > maxPageIndex) {
                pagedElements
            }
            field = value
        }

    override fun renderPanel() {
        genearte()
        super.renderPanel()
    }

    override fun getPage(index: Int): MappedElements {
        if (pagedElements.isEmpty()) addPage(MappedElements())
        return pagedElements[0]
    }

    fun background(function: MappedElements.() -> Unit) {
        function(getStaticElements())
    }

    fun genearte(): Boolean {
        val generated = generator.generate()
        val fromIndex = pageIndex * pool.size
        val toIndex = (fromIndex + pool.size).coerceAtMost(generated.lastIndex)

        return if (generated.size > fromIndex) {
            val elements = generated.also { lastGenerated = it }.subList(fromIndex, toIndex)

            getPage().apply {
                pool.forEachIndexed { index, slot ->
                    if (index <= elements.lastIndex) elements[index].set(slot)
                    else remove(slot)
                }
            }
            true
        } else {
            false
        }
    }

}