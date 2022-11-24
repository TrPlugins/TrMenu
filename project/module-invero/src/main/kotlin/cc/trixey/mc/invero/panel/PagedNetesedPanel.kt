package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.panel.BasePagedPanel
import cc.trixey.mc.invero.common.panel.BasePanel
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/12 22:12
 *
 * 嵌套翻页面板
 * 理论上兼容所有面板模型
 */
class PagedNetesedPanel(scale: ScaleView, weight: PanelWeight) : BasePagedPanel(scale, weight), PanelContainer {

    override val panels: ArrayList<Panel> = ArrayList()

    override var pageIndex = 0
        set(value) {
            if (value in 0..maxPageIndex) field = value
            submit {
                clearPanel()
                renderPanel()
            }
        }

    override val maxPageIndex: Int
        get() = panels.lastIndex

    override fun getChildren() = panels

    override fun renderPanel() = getPage().renderPanel()

    override fun renderElement(window: Window, element: Element) = getPage().renderElement(window, element)

    fun getPage(index: Int = pageIndex) = panels[index]

    override fun getOccupiedSlots(page: Int): Set<Int> {
        return when (val it = getPage(page)) {
            is BasePanel -> it.getOccupiedSlots()
            is BasePagedPanel -> it.getOccupiedSlots(it.pageIndex)
            else -> error("Not yet supported panel type")
        }
    }

    override fun isRenderable(element: Element): Boolean {
        return getPage().isRenderable(element)
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        getPage().handleClick(window, e)
    }

}