package cc.trixey.mc.invero.impl.container.panel

import cc.trixey.mc.invero.Element
import cc.trixey.mc.invero.PanelWeight
import cc.trixey.mc.invero.ScaleView
import cc.trixey.mc.invero.Window
import cc.trixey.mc.invero.item.ElemapCompetent
import cc.trixey.mc.invero.panel.BasePagedPanel
import cc.trixey.mc.invero.impl.container.element.ElemapPaged
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/6 16:30
 *
 * 标准翻页面板
 * 可用理解为多页的 StandardPanel，支持静态、动态元素
 */
open class PagedStandardPanel(scale: ScaleView, weight: PanelWeight) : BasePagedPanel(scale, weight) {

    override fun getRenderability(element: Element): Set<Int> {
        return getPage().findUpperSlots(this, element)
            .ifEmpty { getStaticElements().findUpperSlots(this, element) }
            .toSet()
    }

    override fun renderPanel() {
        forWindows {
            getPage().forEach { renderElement(this, it) }
            getStaticElements().forEach { renderElement(this, it) }
        }
    }

    override var pageIndex = 0
        set(value) {
            if (value in 0..maxPageIndex) field = value.also {
                submit {
                    renderPanel()
                    clearPanel(getUnoccupiedSlots(field))
                }
            }
        }

    override val maxPageIndex: Int
        get() = pagedElements.lastIndex

    internal val pagedElements = ArrayList<ElemapPaged>()

    internal val staticElements by lazy { ElemapCompetent(this) }

    override fun getOccupiedSlots(page: Int): Set<Int> {
        return getPage(page).getOccupiedSlots() + staticElements.getOccupiedSlots()
    }

    open fun getPage(index: Int = pageIndex): ElemapPaged {
        return pagedElements[index]
    }

    open fun getStaticElements(): ElemapCompetent {
        return staticElements
    }

    fun background(function: ElemapCompetent.() -> Unit) {
        staticElements.apply {
            clear()
            function()
        }
    }

    fun addPage(page: ElemapPaged): Int {
        pagedElements += page
        return pagedElements.lastIndex
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true

        e.rawSlot.toLowerSlot()?.let {
            val element = getPage()[it] ?: getStaticElements()[it]
            element?.passClickEvent(e)
        }
    }

}