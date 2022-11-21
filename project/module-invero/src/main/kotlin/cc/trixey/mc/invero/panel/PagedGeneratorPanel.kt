package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.base.BasePagedPanel
import cc.trixey.mc.invero.common.generator.Generator
import cc.trixey.mc.invero.util.distinguishMark
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/21 22:46
 */
class PagedGeneratorPanel(
    scale: PanelScale, pos: Int, weight: PanelWeight
) : BasePagedPanel(scale, pos, weight) {

    /**
     * 元素生成器
     */
    lateinit var generator: Generator<Element>

    /**
     * 页面的元素
     */
    private val page = MappedElements()

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
    private fun generate(): Boolean {
        val elements = generator.spawn()
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
    fun background(function: MappedElements.() -> Unit) {
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
        if (generate()) {
            forWindows {
                page.forEach { renderElement(this, it) }
            }
        }
    }

    override fun renderElement(window: Window, element: Element) {
        if (page.has(element)) {
            val slotMap = getSlotsMap(window)
            if (element is ItemProvider) {
                val itemStack = element.get()
                page.find(element).forEach {
                    val slot = slotMap.getAbsolute(it)
                    window.inventorySet[slot] = itemStack.distinguishMark(slot)
                }
            }
        }
    }

    override fun getOccupiedSlots(page: Int) = this.page.occupiedSlots

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        super.handleClick(window, e)

        val slot = getSlotsMap(window).getRelative(e.rawSlot)
        val element = page[slot]

        element?.passClickEvent(e)
    }

}