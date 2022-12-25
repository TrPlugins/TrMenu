package cc.trixey.mc.invero

import cc.trixey.mc.invero.window.event.InvEvent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/10/29 10:55
 *
 * Window 是提供交互窗口的容器
 * 其包含多个 Panel 子对象，而 UI 的交互与渲染功能都由 Panel 实现, Window 只负责传递与显示
 *
 * 一个 Window 仅允许包含一个 Viewer（唯一的玩家）
 *
 * 注意，如果一个 Panel 被多个 Window 同时调用，应该确保所有 Window 的类型一致
 */
interface Window : Parentable, PanelContainer, Scalable {

    /**
     * 容器的标题
     */
    var title: String

    /**
     * 该容器的类型
     */
    val type: WindowType

    /**
     * 容器规模
     */
    override val scale: ScaleView

    /**
     * 抽象容器管理
     */
    val inventorySet: InventorySet

    /**
     * (安全) 取得该窗口的观众
     */
    fun getViewerSafe(): Player?

    /**
     * 返回当前窗口是否拥有有效的观众
     */
    fun hasViewer() = getViewerSafe() != null

    /**
     * 对交互的反应最低延迟 (ms)
     */
    fun getResponseInterval(): Int

    /**
     * 为该窗口的已指定观众开启窗口
     */
    fun open()

    /**
     * 请求渲染窗口下的所有 Panel
     *
     * @param clearance 是否首先清除窗口
     */
    fun renderWindow(clearance: Boolean = false)

    /**
     * 处理事件
     */
    fun handleEvent(e: InvEvent)

    /**
     * 返回当前观众是否正在查看本窗口
     */
    fun isViewing(): Boolean

}