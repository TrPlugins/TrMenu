package cc.trixey.mc.invero.util.dsl

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.panel.PanelGroup
import cc.trixey.mc.invero.common.panel.PanelInstance
import cc.trixey.mc.invero.common.window.BaseWindow
import cc.trixey.mc.invero.panel.*
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

inline fun <reified T : Panel> PanelContainer.getTypedPanelAt(index: Int): T = panels.filterIsInstance<T>()[index]

inline fun <reified T : Panel> PanelContainer.firstPanel(): T = getTypedPanelAt(0)

fun PanelContainer.getScrollPanel(index: Int = 0): ScrollStandardPanel = getTypedPanelAt(index)

fun PanelContainer.getScrollGeneratorPanel(index: Int = 0): ScrollGeneratorPanel = getTypedPanelAt(index)

fun PanelContainer.getPagedPanel(index: Int = 0): PagedStandardPanel = getTypedPanelAt(index)

fun PanelContainer.getPagedGeneratorPanel(index: Int = 0): PagedGeneratorPanel = getTypedPanelAt(index)

fun PanelContainer.getPagedNetesedPanel(index: Int = 0): PagedNetesedPanel = getTypedPanelAt(index)

fun PanelContainer.getPanelGroup(index: Int = 0): PanelGroup = getTypedPanelAt(index)

fun PanelContainer.getStandardPanel(index: Int = 0): StandardPanel = getTypedPanelAt(index)

fun PanelContainer.autoPositioning(): Int {
    val available = when (this) {
        is Window -> if (inventorySet.isCompleteSet) type.slotsEntireWindow else type.slotsContainer
        is PanelInstance -> slots
        else -> error("Unsupported PanelContainer to get first position")
    }.sorted().toMutableList()

    if (this !is PagedNetesedPanel) available -= panels.flatMap { panel ->
        val scale = panel.scale
        val parent = (this as Scalable).scale
        scale.slots.map { scale.getUpperSlot(parent, it) }
    }.toSet()

    return available.first()
}

fun BaseWindow.title(value: String) {
    title = value
}

/**
 * Window 下创建 Paenl 函数
 */
inline fun PanelContainer.scroll(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: ScrollStandardPanel.() -> Unit
) = cc.trixey.mc.invero.util.dsl.scroll(scale, pos, weight, block).also { add(it) }

inline fun PanelContainer.standard(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) = cc.trixey.mc.invero.util.dsl.standard(scale, pos, weight, block).also { add(it) }

inline fun PanelContainer.paged(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedStandardPanel.() -> Unit
) = cc.trixey.mc.invero.util.dsl.paged(scale, pos, weight, block).also { add(it) }

inline fun PanelContainer.pagedNetesed(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedNetesedPanel.() -> Unit
) = cc.trixey.mc.invero.util.dsl.pagedNetesed(scale, pos, weight, block).also { add(it) }

inline fun PanelContainer.generatorScroll(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: ScrollGeneratorPanel.() -> Unit
) = cc.trixey.mc.invero.util.dsl.generatorScroll(scale, pos, weight, block).also { add(it) }

inline fun PanelContainer.generatorPaged(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedGeneratorPanel.() -> Unit
) = cc.trixey.mc.invero.util.dsl.generatorPaged(scale, pos, weight, block).also { add(it) }

inline fun PanelContainer.storageIOPanel(
    scale: Pair<Int, Int>,
    pos: Int = autoPositioning(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: IOStoragePanel.() -> Unit
) = cc.trixey.mc.invero.util.dsl.storageIOPanel(scale, pos, weight, block).also { add(it) }

inline fun scroll(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: ScrollStandardPanel.() -> Unit
) = buildPanel<ScrollStandardPanel>(scale, pos, weight).also(block)

inline fun standard(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) = buildPanel<StandardPanel>(scale, pos, weight).also(block)

inline fun paged(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedStandardPanel.() -> Unit
) = buildPanel<PagedStandardPanel>(scale, pos, weight).also(block)

inline fun pagedNetesed(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedNetesedPanel.() -> Unit
) = buildPanel<PagedNetesedPanel>(scale, pos, weight).also(block)

inline fun generatorScroll(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: ScrollGeneratorPanel.() -> Unit
) = buildPanel<ScrollGeneratorPanel>(scale, pos, weight).also(block)

inline fun generatorPaged(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: PagedGeneratorPanel.() -> Unit
) = buildPanel<PagedGeneratorPanel>(scale, pos, weight).also(block)

inline fun storageIOPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: IOStoragePanel.() -> Unit
) = buildPanel<IOStoragePanel>(scale, pos, weight).also(block)

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
