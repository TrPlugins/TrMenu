package cc.trixey.mc.invero.impl.container.panel

import cc.trixey.mc.invero.*
import cc.trixey.mc.invero.panel.BasePagedPanel
import cc.trixey.mc.invero.panel.BasePanel
import cc.trixey.mc.invero.panel.BaseScrollPanel
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
    override fun getRenderability(element: Element) = getPage().getRenderability(element)
    override fun handleClick(window: Window, e: InventoryClickEvent) = getPage().handleClick(window, e)

    fun getPage(index: Int = pageIndex) = panels[index]

    override fun getOccupiedSlots(page: Int): Set<Int> {
        return when (val it = getPage(page)) {
            is BasePanel -> it.getOccupiedSlots()
            is BasePagedPanel -> it.getOccupiedSlots(it.pageIndex)
            is BaseScrollPanel -> it.getOccupiedSlots()
            else -> error("Not yet supported this panel type")
        }
    }

}