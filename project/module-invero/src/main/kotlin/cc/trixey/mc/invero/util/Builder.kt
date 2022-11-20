package cc.trixey.mc.invero.util

import cc.trixey.mc.invero.InveroManager.constructElement
import cc.trixey.mc.invero.InveroManager.constructPanel
import cc.trixey.mc.invero.InveroManager.constructWindow
import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.common.base.BasePanel
import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.base.ElementDynamic
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
    viewer: Player, type: WindowType = WindowType.ofRows(6), title: String = "Untitled", init: T.() -> Unit = {}
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

inline fun <reified T : Element> Panel.buildElement(init: T.() -> Unit = {}): T {
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

inline fun <reified T : Element> BasePanel.addElement(vararg slots: Int, init: T.() -> Unit = {}): T {
    return addElement(slots.toSet(), init)
}

inline fun <reified T : Element> BasePanel.addElement(slots: Collection<Int>, init: T.() -> Unit = {}): T {
    return buildElement(init).also { element ->
        if (slots.isNotEmpty() && element is ElementAbsolute) {
            slots.forEach {
                if (it < 0) error("Relative slot can not be negative number")
                set(it, element)
            }
        } else if (element is ElementDynamic) {
            getElementsMap() += element
        }
    }
}