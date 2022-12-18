package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.ScaleView
import cc.trixey.mc.invero.common.item.ElemapSimplified
import cc.trixey.mc.invero.common.item.ElementAbsolute
import cc.trixey.mc.invero.common.panel.BasePagedPanel
import cc.trixey.mc.invero.common.panel.generator.GeneratorPanel
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/21 22:46
 *
 * 翻页生成器面板
 * 根据页码生成静态元素覆盖生成池槽位
 * 支持设置静态物品（槽位自动排除生成池）
 */
class PagedGeneratorPanel(scale: ScaleView, weight: PanelWeight) : BasePagedPanel(scale, weight), GeneratorPanel {

    /**
     * 最后一次生成的元素
     */
    override var lastGenerated: List<ElementAbsolute> = listOf()

    /**
     * 页面的元素
     */
    private val page = ElemapSimplified(this)

    /**
     * 静态槽位（排除生成池槽位）
     */
    private val static: MutableSet<Int> = mutableSetOf()

    /**
     * 生成池槽位（用以应用生成的元素）
     */
    private var pool = slots - static

    override var pageIndex: Int = 0
        set(value) {
            if (value in 0..maxPageIndex) field = value.also {
                submit {
                    renderPanel()
                    clearPanel(getUnoccupiedSlots(field))
                }
            }
        }

    override var maxPageIndex: Int = 0

    /**
     * 生成元素、裁切针对当前页码的有效元素集
     * 并设置元素
     */
    private fun apply(): Boolean {
        val elements = lastGenerated
        val fromIndex = pageIndex * pool.size
        val toIndex = (fromIndex + pool.size).coerceAtMost(elements.lastIndex)
        maxPageIndex = elements.size / pool.size

        if (elements.size > fromIndex) {
            val apply = elements.subList(fromIndex, toIndex)
            page.apply {
                pool.forEachIndexed { index, slot ->
                    if (index <= apply.lastIndex) apply[index].set(slot)
                    else remove(slot)
                }
            }
            return true
        }
        return false
    }

    /**
     * 设置背景静态元素
     */
    fun background(function: ElemapSimplified.() -> Unit) {
        page.apply {
            clear()
            function()
            forEachSloted { _, slots ->
                slots.forEach { exclude(it) }
            }
        }
    }

    /**
     * 设置静态槽位
     */
    fun exclude(slot: Int) {
        static += slot
        pool = slots - static
    }

    override fun renderPanel() {
        if (!apply()) return
        forWindows { page.forEach { renderElement(this, it) } }
    }

    override fun getRenderability(element: Element) = page.findUpperSlots(this, element)

    override fun getOccupiedSlots(page: Int) = this.page.getOccupiedSlots()

}