package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.base.BasePagedPanel
import cc.trixey.mc.invero.util.distinguishMark
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/6 16:30
 *
 * 标准翻页面板
 * 可用理解为多页的 StandardPanel，支持静态、动态元素
 */
open class PagedStandardPanel(
    scale: PanelScale,
    pos: Int,
    weight: PanelWeight
) : BasePagedPanel(scale, pos, weight) {

    override fun renderPanel() {
        forWindows {
            getPage().forEach { renderElement(this, it) }
            getStaticElements().forEach { renderElement(this, it) }
        }
    }

    override fun renderElement(window: Window, element: Element) {
        if (!isRenderable(element)) return

        val slotMap = getSlotsMap(window)
        if (element is ItemProvider) {
            val itemStack = element.get()
            findSlots(element).forEach {
                val slot = slotMap.getAbsolute(it)
                window.inventorySet[slot] = itemStack.distinguishMark(slot)
            }
        }
    }

    private fun findSlots(element: Element): Set<Int> {
        return getPage().find(element).ifEmpty { getStaticElements().find(element) }
    }

    /**
     * @see BasePagedPanel.pageIndex
     */
    override var pageIndex = 0
        set(value) {
            if (value in 0..maxPageIndex) field = value.also {
                submit {
                    renderPanel()
                    clearPanel(getUnoccupiedSlots(field))
                }
            }
        }

    /**
     * @see BasePagedPanel.maxPageIndex
     */
    override val maxPageIndex: Int
        get() = pagedElements.lastIndex

    /**
     * 多页元素
     */
    internal val pagedElements = ArrayList<ElemapCompetent>()

    /**
     * 静态占据元素
     */
    internal val staticElements = ElemapCompetent()

    /**
     * @see BasePagedPanel.getOccupiedSlots
     */
    override fun getOccupiedSlots(page: Int): Set<Int> {
        return getPage(page).getOccupiedSlots() + staticElements.getOccupiedSlots()
    }

    /**
     * 取得指定页码的元素
     */
    open fun getPage(index: Int = pageIndex) = pagedElements[index]

    /**
     * 取得静态占据元素
     */
    open fun getStaticElements() = staticElements

    /**
     * 设置背景静态元素
     */
    fun background(function: ElemapCompetent.() -> Unit) {
        staticElements.apply {
            clear()
            function()
        }
    }

    /**
     * 添加一页元素
     */
    fun addPage(page: ElemapCompetent): Int {
        pagedElements += page
        return pagedElements.lastIndex
    }

    /**
     * @see BasePagedPanel.isRenderable
     */
    override fun isRenderable(element: Element): Boolean {
        return getPage().has(element) || getStaticElements().has(element)
    }

    /**
     * 处理点击事件
     */
    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true

        val slot = getSlotsMap(window).getRelative(e.rawSlot)
        val element = getPage()[slot] ?: getStaticElements()[slot]

        element?.passClickEvent(e)
    }

}