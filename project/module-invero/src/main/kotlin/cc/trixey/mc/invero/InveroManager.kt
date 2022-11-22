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
 * [ ] IOStoragePanel & 物品交互模拟
 * [ ] PacketWindow 虚拟容器的实现和Bukkit事件抽象化兼容
 * [ ] 需要改进开放性注册
 */
object InveroManager {

    private val runningWindows = CopyOnWriteArrayList<Window>()

    fun register(window: Window) {
        window.panels.forEach { it.registerWindow(window) }
        runningWindows.add(window)
    }

    fun unregister(window: Window): Boolean {
        window.panels.forEach { it.unregisterWindow(window) }
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
        return constructPanel(type, scale.toScale(), posMark, weight)
    }

    fun constructPanel(type: Class<*>, scale: PanelScale, posMark: Int, weight: PanelWeight): Panel {
        return when (type) {
            StandardPanel::class.java -> StandardPanel(scale, posMark, weight)
            PagedStandardPanel::class.java -> PagedStandardPanel(scale, posMark, weight)
            PagedNetesedPanel::class.java -> PagedNetesedPanel(scale, posMark, weight)
            PagedGeneratorPanel::class.java -> PagedGeneratorPanel(scale, posMark, weight)
            ScrollStandardPanel::class.java -> ScrollStandardPanel(scale, posMark, weight)
            ScrollGeneratorPanel::class.java -> ScrollGeneratorPanel(scale, posMark, weight)
            IOStoragePanel::class.java -> IOStoragePanel(scale, posMark, weight)
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