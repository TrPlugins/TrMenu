package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.PanelScale
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.Window
import cc.trixey.mc.invero.common.base.BaseScrollPanel
import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType

/**
 * @author Arasple
 * @since 2022/11/20 18:06
 */
class ScrollGeneratorPanel(
    scale: PanelScale,
    pos: Int,
    weight: PanelWeight,
    direction: ScrollDirection = ScrollDirection(vertical = true),
    type: ScrollType = ScrollType.STOP
) : BaseScrollPanel(scale, pos, weight, direction, type) {

    override var index: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override val maxIndex: Int
        get() = TODO("Not yet implemented")

    override fun renderPanel() {
        TODO("Not yet implemented")
    }

    override fun renderElement(window: Window, element: Element) {
        TODO("Not yet implemented")
    }

}