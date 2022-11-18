package cc.trixey.mc.trmenu.invero.util

import cc.trixey.mc.trmenu.invero.InveroManager.constructElement
import cc.trixey.mc.trmenu.invero.InveroManager.constructPanel
import cc.trixey.mc.trmenu.invero.InveroManager.constructWindow
import cc.trixey.mc.trmenu.invero.impl.panel.PagedNetesedPanel
import cc.trixey.mc.trmenu.invero.impl.panel.PagedStandardPanel
import cc.trixey.mc.trmenu.invero.module.*
import cc.trixey.mc.trmenu.invero.module.base.BasePanel
import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute
import cc.trixey.mc.trmenu.invero.module.element.ElementDynamic
import cc.trixey.mc.trmenu.invero.module.element.ItemProvider
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder

/**
 * @author Arasple
 * @since 2022/11/5 15:50
 */

/**
 * Building functions
 */

inline fun <reified T : Window> buildWindow(
    viewer: Player, type: TypeAddress = TypeAddress.ofRows(6), title: String = "Untitled", init: T.() -> Unit = {}
): T {
    return (constructWindow(T::class.java, viewer, type, title) as T).also(init)
}

inline fun <reified T : Panel> buildPanel(
    scale: Pair<Int, Int>, pos: Int = 0, weight: PanelWeight = PanelWeight.NORMAL, init: T.() -> Unit = {}
): T {
    return (constructPanel(T::class.java, scale, pos, weight) as T).also(init)
}

inline fun <reified T : Panel> buildPanel(
    scale: List<Int>, pos: Int = 0, weight: PanelWeight = PanelWeight.NORMAL, init: T.() -> Unit = {}
) = buildPanel(scale.first() to scale.last(), pos, weight, init)

inline fun <reified T : PanelElement> Panel.buildElement(init: T.() -> Unit = {}): T {
    return (constructElement(T::class.java) as T).also(init)
}

inline fun <reified T : ItemProvider> Panel.buildItem(itemStack: ItemStack, init: T.() -> Unit = {}): T {
    return (constructElement(T::class.java) as T).also {
        it.setItem(itemStack)
        init(it)
    }
}

inline fun <reified T : ItemProvider> Panel.buildItem(
    material: Material, noinline builder: ItemBuilder.() -> Unit = {}, init: T.() -> Unit = {}
) = buildItem(taboolib.platform.util.buildItem(material, builder), init)

fun Window.addPanel(vararg panel: Panel) {
    panel.forEach { panels += it }
}

/**
 * Adding functions
 */

inline fun <reified T : Panel> Window.addPanel(
    scale: Pair<Int, Int>, pos: Int = 0, weight: PanelWeight = PanelWeight.NORMAL, init: T.() -> Unit = {}
): T {
    return (constructPanel(T::class.java, scale, pos, weight) as T).also(init).also {
        panels += it
    }
}

fun ElementAbsolute.setDefault(panel: PagedStandardPanel, vararg slots: Int) {
    panel.getFallbackElements().apply { set(*slots) }
}

fun PagedStandardPanel.page(function: MappedElements.(Int) -> Unit) {
    return MappedElements().let { function(it, addPage(it)) }
}

fun PagedNetesedPanel.page(panel: PanelInstance) {
    addPage(panel)
}

fun PanelInstance.paged(parent: PagedNetesedPanel) {
    parent.page(this)
}

inline fun <reified T : PanelElement> BasePanel.addElement(vararg slots: Int, init: T.() -> Unit = {}): T {
    return addElement(slots.toSet(), init)
}

inline fun <reified T : PanelElement> BasePanel.addElement(slots: Collection<Int>, init: T.() -> Unit = {}): T {
    return buildElement(init).also { element ->
        if (slots.isNotEmpty() && element is ElementAbsolute) {
            slots.forEach {
                if (it < 0) throw IllegalArgumentException("Relative slot can not be negative number")
                setElement(it, element)
            }
        } else if (element is ElementDynamic) {
            addDynamicElement(element)
        }
    }
}