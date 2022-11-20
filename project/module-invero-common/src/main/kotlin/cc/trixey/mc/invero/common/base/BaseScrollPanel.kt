package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.scroll.ScrollDirection
import cc.trixey.mc.invero.common.scroll.ScrollType

/**
 * @author Arasple
 * @since 2022/11/17 11:19
 */
abstract class BaseScrollPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight,
    val direction: ScrollDirection,
    val scrollType: ScrollType
) : BasePanel(scale, pos, weight) {

    /**
     * Current index of the scrolling page
     */
    abstract var index: Int

    abstract fun scroll(step: Int = 1)

}
