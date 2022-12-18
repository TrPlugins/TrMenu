package cc.trixey.mc.invero

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.element.BasicDynamicItem
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.*
import cc.trixey.mc.invero.util.toScale
import cc.trixey.mc.invero.window.CompleteWindow
import cc.trixey.mc.invero.window.ContainerWindow
import org.bukkit.entity.Player
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/10/28 19:22
 *
 * Roadmap
 *
 * [√] ScrollGeneratorPanel
 * [√] DSL
 * [ ] 元素结构实现的优化（Absolute/Dynamic），SlotMap与嵌套Panel的优化
 * [ ] InfiniteCavansPanel 的实现
 * [ ] IOStoragePanel & 物品交互模拟
 * [ ] PacketWindow 虚拟容器的实现和Bukkit事件抽象化兼容
 */
object InveroManager {

    private val runningWindows = CopyOnWriteArrayList<Window>()

    fun register(window: Window) {
        window.forEachPanel { registerWindow(window) }
        runningWindows.add(window)
    }

    fun unregister(window: Window): Boolean {
        window.forEachPanel { unregisterWindow(window) }
        return runningWindows.remove(window)
    }

    fun getWindows() = runningWindows

    fun Panel.constructElement(type: Class<*>): Element {
        return when (type) {
            BasicItem::class.java -> BasicItem(null, this)
            BasicDynamicItem::class.java -> BasicDynamicItem(null, this)
            else -> throw IllegalArgumentException("Failed to create element, not found registered class $type")
        }
    }

    fun constructPanel(type: Class<*>, scale: Pair<Int, Int>, posMark: Int, weight: PanelWeight): Panel {
        return constructPanel(type, scale.toScale(posMark), weight)
    }

    fun constructPanel(type: Class<*>, scale: ScaleView, weight: PanelWeight): Panel {
        return when (type) {
            StandardPanel::class.java -> StandardPanel(scale, weight)
            PagedStandardPanel::class.java -> PagedStandardPanel(scale, weight)
            PagedNetesedPanel::class.java -> PagedNetesedPanel(scale, weight)
            PagedGeneratorPanel::class.java -> PagedGeneratorPanel(scale, weight)
            ScrollStandardPanel::class.java -> ScrollStandardPanel(scale, weight)
            ScrollGeneratorPanel::class.java -> ScrollGeneratorPanel(scale, weight)
            FreeformNetesedPanel::class.java -> FreeformNetesedPanel(scale, weight)
            IOStoragePanel::class.java -> IOStoragePanel(scale, weight)
            else -> throw IllegalArgumentException("Failed to create panel, not found registered class $type")
        }
    }

    fun constructWindow(type: Class<*>, viewer: Player, windowType: WindowType, title: String): Window {
        return when (type) {
            ContainerWindow::class.java -> ContainerWindow(viewer, windowType, title)
            CompleteWindow::class.java -> CompleteWindow(viewer, windowType, title)
            else -> throw IllegalArgumentException("Failed to create window, not found registered class $type")
        }
    }

}