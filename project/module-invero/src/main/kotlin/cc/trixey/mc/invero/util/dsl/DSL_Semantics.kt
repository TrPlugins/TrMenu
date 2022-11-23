package cc.trixey.mc.invero.util.dsl

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.base.BaseWindow
import cc.trixey.mc.invero.panel.StandardPanel

/**
 * @author Arasple
 * @since 2022/11/23 11:03
 *
 * 通用语义性创建
 *
 */

/**
 * 创建导航条，实际是创建一个 StandardPanel
 */
inline fun BaseWindow.nav(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) = standard(scale, pos, weight, block)