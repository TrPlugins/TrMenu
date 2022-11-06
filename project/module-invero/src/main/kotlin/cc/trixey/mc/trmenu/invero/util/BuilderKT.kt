package cc.trixey.mc.trmenu.invero.util

import cc.trixey.mc.trmenu.invero.InveroManager.constructElement
import cc.trixey.mc.trmenu.invero.InveroManager.constructPanel
import cc.trixey.mc.trmenu.invero.InveroManager.constructWindow
import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.PanelElement
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/11/5 15:50
 */
inline fun <reified T : Window> buildWindow(
    viewer: Player, title: String = "Untitled", windowType: TypeAddress = TypeAddress.ofRows(6), init: T.() -> Unit = {}
): T {
    return (constructWindow(T::class.java, viewer, title, windowType) as T).also(init)
}


fun Window.addPanel(panel: Panel) {
    panels += panel
}

inline fun <reified T : Panel> Window.addPanel(
    size: Pair<Int, Int> = 3 to 3, posMark: Int = 0, init: T.() -> Unit = {}
): T {
    return (constructPanel(T::class.java, size, posMark) as T).also(init).also {
        panels += it
    }
}

inline fun <reified T : Panel> createPanel(
    size: Pair<Int, Int> = 3 to 3, posMark: Int = 0, init: T.() -> Unit = {}
): T {
    return (constructPanel(T::class.java, size, posMark) as T).also(init)
}

inline fun <reified T : PanelElement> Panel.addElement(vararg relativeSlot: Int, init: T.() -> Unit = {}): T {
    return (constructElement(T::class.java) as T).also(init).also { element ->
        if (relativeSlot.isNotEmpty()) {
            relativeSlot.forEach {
                if (it < 0) throw IllegalArgumentException("Relative slot can not be negative number")
                setElement(it, element)
            }
        }
    }
}
