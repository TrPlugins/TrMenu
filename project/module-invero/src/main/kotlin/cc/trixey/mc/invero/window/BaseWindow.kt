package cc.trixey.mc.invero.window

import cc.trixey.mc.invero.Panel
import cc.trixey.mc.invero.Parentable
import cc.trixey.mc.invero.Window
import org.bukkit.entity.Player
import taboolib.common.platform.function.getProxyPlayer
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/29 11:34
 * @see Window
 *
 * 基础的 Window 抽象实例
 */
abstract class BaseWindow(val viewer: UUID) : Window {

    override val panels: ArrayList<Panel> = ArrayList()

    override fun getChildren() = panels

    override fun getResponseInterval() = 200

    fun getViewer() = getViewerSafe() ?: throw NullPointerException("Expected viewer is not valid")
    override fun getViewerSafe() = getProxyPlayer(viewer)?.castSafely<Player>()

    override fun renderWindow(clearance: Boolean) {
        if (!hasViewer()) error("Unable to render this panel while the viewer does not exists")
        if (clearance) inventorySet.clear()

        // TODO 改进渲染逻辑
        // 重叠区域、不必要渲染区域省去
        panels.sortedBy { it.weight }.forEach { panel ->
            panel.renderPanel()
        }
    }

    override fun setParent(parentable: Parentable) {
        error("Window can not set parent")
    }

}