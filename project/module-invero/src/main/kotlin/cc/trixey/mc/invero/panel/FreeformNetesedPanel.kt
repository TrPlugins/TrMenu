package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.panel.PanelGroup
import cc.trixey.mc.invero.common.panel.freeform.FreeformPanel

/**
 * @author Arasple
 * @since 2022/11/23 13:07
 */
class FreeformNetesedPanel(
    scale: ScaleView, weight: PanelWeight
) : PanelGroup(scale, weight), PanelContainer, FreeformPanel {

    override val panels: ArrayList<Panel> = ArrayList()

    override fun renderPanel() {
        forEachPanel { renderPanel() }
    }

    override fun renderElement(window: Window, element: Element) {
        error("FreeformNetesedPanel itself does not support getRenderability()")
    }

    override fun getRenderability(element: Element): Set<Int> {
        error("FreeformNetesedPanel itself does not support renderElement()")
    }

    override fun shift(x: Int, y: Int) {
        panels.forEach {
            // 定位 Panel 的坐标
            val (cX, cY) = scale.locate(it.scale.position)
            // 移动加值
            val (rX, rY) = cX - x to cY - y
            // 设置 Panel 的坐标
            it.scale.position = rY * scale.width + rX
        }
        clearPanel()
        renderPanel()
    }

    override fun reset() {
//        viewPosition = defaultPosition
    }

}