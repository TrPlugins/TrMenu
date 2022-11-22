package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.Parentable
import cc.trixey.mc.invero.common.Window
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

    /**
     * 该窗口使用的 Panels
     */
    override val panels: ArrayList<Panel> = ArrayList()

    override fun getResponseInterval(): Int {
        return 200
    }

    operator fun plusAssign(value: PanelInstance) {
        panels += value.also { it.setParent(this) }
    }

    operator fun minusAssign(value: Panel) {
        panels -= value
    }

    fun getViewer(): Player {
        return getViewerSafe() ?: throw NullPointerException("Expected viewer is not valid")
    }

    override fun getViewerSafe(): Player? {
        return getProxyPlayer(viewer)?.castSafely()
    }

    override fun renderWindow(clearance: Boolean) {
        if (!hasViewer()) error("Unable to render this panel while the viewer does not exists")
        if (clearance) inventorySet.clear()

        // TODO 改进渲染逻辑
        // 重叠区域、不必要渲染区域省去
        panels.sortedBy { it.weight }.forEach { panel ->
            panel.renderPanel()
        }
    }

    override fun setParent(parentable: Parentable?) {
        error("Window can not set parent")
    }

}