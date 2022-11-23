package cc.trixey.mc.invero.util.dsl

import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.WindowType
import cc.trixey.mc.invero.common.base.BaseWindow
import cc.trixey.mc.invero.common.universal.PanelGroup
import cc.trixey.mc.invero.panel.*
import cc.trixey.mc.invero.util.addPanel
import cc.trixey.mc.invero.util.buildPanel
import cc.trixey.mc.invero.util.buildWindow
import cc.trixey.mc.invero.window.CompleteWindow
import cc.trixey.mc.invero.window.ContainerWindow
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/11/23 11:00
 */

/**
 * BaseWindow 的增强拓展函数
 */

@Suppress("UNCHECKED_CAST")
fun <T : Panel> BaseWindow.panelAt(index: Int): T = panels[index] as T

fun <T : Panel> BaseWindow.firstPanel(): T = panelAt(0)

fun BaseWindow.firstScrollPanel(): ScrollStandardPanel = firstPanel()

fun BaseWindow.firstScrollGeneratorPanel(): ScrollGeneratorPanel = firstPanel()

fun BaseWindow.firstPagedPanel(): PagedStandardPanel = firstPanel()

fun BaseWindow.firstPagedGeneratorPanel(): PagedGeneratorPanel = firstPanel()

fun BaseWindow.firstPagedNetesedPanel(): PagedNetesedPanel = firstPanel()

fun BaseWindow.firstPanelGroup(): PanelGroup = firstPanel()

fun BaseWindow.firstStandardPanel(): StandardPanel = firstPanel()

fun BaseWindow.firstPos(): Int {
    val total = if (inventorySet.isCompleteSet) type.slotsEntireWindow else type.slotsContainer
    val available = total.toSet() - panels.flatMap { it.getSlotsMap(this).absoluteSlots }.toSet()
    return available.first()
}

fun BaseWindow.title(value: String) {
    title = value
}

/**
 * Window 下创建 Paenl 函数
 */
inline fun BaseWindow.scroll(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: ScrollStandardPanel.() -> Unit
) = buildPanel<ScrollStandardPanel>(scale, pos, weight).also(block).also { addPanel(it) }

inline fun BaseWindow.standard(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) = buildPanel<StandardPanel>(scale, pos, weight).also(block).also { addPanel(it) }

inline fun BaseWindow.paged(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedStandardPanel.() -> Unit
) = buildPanel<PagedStandardPanel>(scale, pos, weight).also(block).also { addPanel(it) }

inline fun BaseWindow.pagedNetesed(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedNetesedPanel.() -> Unit
) = buildPanel<PagedNetesedPanel>(scale, pos, weight).also(block).also { addPanel(it) }


inline fun BaseWindow.generatorScroll(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: ScrollGeneratorPanel.() -> Unit
) = buildPanel<ScrollGeneratorPanel>(scale, pos, weight).also(block).also { addPanel(it) }

inline fun BaseWindow.generatorPaged(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedGeneratorPanel.() -> Unit
) = buildPanel<PagedGeneratorPanel>(scale, pos, weight).also(block).also { addPanel(it) }

inline fun BaseWindow.storageIOPanel(
    scale: Pair<Int, Int>,
    pos: Int = firstPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: IOStoragePanel.() -> Unit
) = buildPanel<IOStoragePanel>(scale, pos, weight).also(block).also { addPanel(it) }

/**
 * 创建 Window 的函数
 */

inline fun containerWindow(
    viewer: Player,
    type: WindowType = WindowType.ofRows(6),
    title: String = "Untitled",
    block: ContainerWindow.() -> Unit
): ContainerWindow {
    return buildWindow<ContainerWindow>(viewer, type, title).also(block)
}

inline fun completeWindow(
    viewer: Player,
    type: WindowType = WindowType.ofRows(6),
    title: String = "Untitled",
    block: CompleteWindow.() -> Unit
): CompleteWindow {
    return buildWindow<CompleteWindow>(viewer, type, title).also(block)
}

inline fun window(
    viewer: Player,
    type: WindowType = WindowType.ofRows(6),
    title: String = "Untitled",
    block: ContainerWindow.() -> Unit
): ContainerWindow {
    return containerWindow(viewer, type, title, block)
}
