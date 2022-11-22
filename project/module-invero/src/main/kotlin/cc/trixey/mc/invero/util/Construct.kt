package cc.trixey.mc.invero.util

import cc.trixey.mc.invero.InveroManager
import cc.trixey.mc.invero.InveroManager.constructElement
import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.base.BasePanel
import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.base.ElementDynamic
import cc.trixey.mc.invero.common.base.PanelInstance
import cc.trixey.mc.invero.common.universal.PanelGroup
import cc.trixey.mc.invero.panel.PagedNetesedPanel
import cc.trixey.mc.invero.panel.PagedStandardPanel
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder

/**
 * @author Arasple
 * @since 2022/11/22 21:55
 */

/**
 * 构建通用 Window 的函数
 */
inline fun <reified T : Window> buildWindow(
    viewer: Player,
    type: WindowType = WindowType.ofRows(6),
    title: String = "Untitled",
    init: T.() -> Unit = {}
): T {
    return (InveroManager.constructWindow(T::class.java, viewer, type, title) as T).also(init)
}

/**
 * 构建通用 Panel 的函数
 */
inline fun <reified T : Panel> buildPanel(
    scale: Pair<Int, Int>,
    pos: Int = 0,
    weight: PanelWeight = PanelWeight.NORMAL,
    init: T.() -> Unit = {}
): T {
    return (InveroManager.constructPanel(T::class.java, scale, pos, weight) as T).also(init)
}

/**
 * 在 Panel 下 构建通用 Element 的函数
 */
inline fun <reified T : Element> Panel.buildElement(init: T.() -> Unit = {}): T {
    return (constructElement(T::class.java) as T).also(init)
}


/**
 * 在 Panel 下 构建通用 ItemProvider 的函数
 */
inline fun <reified T : ItemProvider> Panel.buildItem(
    itemStack: ItemStack,
    init: T.() -> Unit = {}
): T {
    return (constructElement(T::class.java) as T).also {
        it.setItem(itemStack)
        init(it)
    }
}

/**
 * 在 Panel 下 构建通用 ItemProvider 的函数
 */
inline fun <reified T : ItemProvider> Panel.buildItem(
    material: Material,
    noinline builder: ItemBuilder.() -> Unit = {},
    init: T.() -> Unit = {}
) = buildItem(taboolib.platform.util.buildItem(material, builder), init)

/**
 * 为 Window 添加 Panel 面板的功能
 */
fun Window.addPanel(vararg panel: Panel) {
    panel.forEach { panels += it }
}

/**
 * 构建 Window 下适用的添加 构建面板函数
 */
inline fun <reified T : Panel> Window.addPanel(
    scale: Pair<Int, Int>,
    pos: Int = 0,
    weight: PanelWeight = PanelWeight.NORMAL,
    init: T.() -> Unit = {}
): T {
    return buildPanel(scale, pos, weight, init).also { addPanel(it) }
}

/**
 * 构建 Paenl 下添加构建元素的函数
 */
inline fun <reified T : Element> BasePanel.addElement(
    vararg slots: Int,
    init: T.() -> Unit = {}
): T {
    return addElement(slots.toSet(), init)
}

/**
 * 构建 Panel 下添加构建元素的函数
 */
inline fun <reified T : Element> BasePanel.addElement(
    slots: Collection<Int>,
    init: T.() -> Unit = {}
): T {
    return buildElement(init).also { element ->
        val add = slots.filter { it >= 0 }.toSet()
        when (element) {
            is ElementDynamic -> element.slots(add)
            is ElementAbsolute -> element.set(add)
            else -> error("addElement() failed")
        }
    }
}

/**
 * 在 PagedStandardPanel 下通过 page { } 构建元素页的函数
 */
fun PagedStandardPanel.page(function: ElemapCompetent.(Int) -> Unit) {
    return ElemapCompetent().let { function(it, addPage(it)) }
}

/**
 * PairInt 转 PanelScale
 */
fun Pair<Int, Int>.toScale(): PanelScale {
    return PanelScale(this)
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