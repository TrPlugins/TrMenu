package cc.trixey.mc.invero.util

import cc.trixey.mc.invero.common.ElemapCompetent
import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.Window
import cc.trixey.mc.invero.common.base.PanelInstance
import cc.trixey.mc.invero.common.universal.PanelGroup
import cc.trixey.mc.invero.panel.PagedNetesedPanel
import cc.trixey.mc.invero.panel.PagedStandardPanel

/**
 * @author Arasple
 * @since 2022/11/22 21:55
 */

/**
 * 为 Window 添加 Panel 面板的功能
 */
fun Window.addPanel(vararg panel: Panel) {
    panel.forEach { panels += it }
}

/**
 * 在 PagedStandardPanel 下通过 page { } 构建元素页的函数
 */
fun PagedStandardPanel.page(function: ElemapCompetent.(Int) -> Unit) {
    return ElemapCompetent().let { function(it, addPage(it)) }
}

/**
 * 创建 PanelGroup
 */
fun group(
    scale: Pair<Int, Int>,
    pos: Int = 0,
    weight: PanelWeight = PanelWeight.NORMAL,
    function: PanelGroup.() -> Unit
): PanelGroup {
    return PanelGroup(scale.toScale(), pos, weight).also(function)
}

/**
 * 构建 PanelGroup 时的 addPaenl 函数
 */
inline fun <reified T : PanelInstance> PanelGroup.addPanel(
    scale: Pair<Int, Int>, pos: Int = 0, weight: PanelWeight = PanelWeight.NORMAL, init: T.() -> Unit = {}
): T {
    return buildPanel(scale, pos, weight, init).also { it.grouped() }
}

/**
 * 构建 PagedNetesedPanel 时的 addPaenl 函数
 */
inline fun <reified T : PanelInstance> PagedNetesedPanel.addPanel(
    scale: Pair<Int, Int>,
    pos: Int = 0,
    weight: PanelWeight = PanelWeight.NORMAL,
    init: T.() -> Unit = {}
): T {
    return buildPanel(scale, pos, weight, init).also { addPage(it) }
}

/**
 * 在 PagedNetesedPanel 下 创建 PanelGroup
 * 自动添加到嵌套页中
 */
fun PagedNetesedPanel.addGroup(
    scale: Pair<Int, Int>,
    pos: Int = 0,
    weight: PanelWeight = PanelWeight.NORMAL,
    function: PanelGroup.() -> Unit
): PanelGroup {
    return group(scale, pos, weight, function).also { addPage(it) }
}