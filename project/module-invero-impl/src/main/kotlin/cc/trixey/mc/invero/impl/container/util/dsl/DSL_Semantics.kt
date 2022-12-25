package cc.trixey.mc.invero.impl.container.util.dsl

import cc.trixey.mc.invero.PanelContainer
import cc.trixey.mc.invero.PanelWeight
import cc.trixey.mc.invero.impl.container.panel.StandardPanel

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
inline fun PanelContainer.nav(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) = standard(scale, pos, weight, block)