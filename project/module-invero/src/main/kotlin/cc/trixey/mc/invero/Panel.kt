package cc.trixey.mc.invero

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * @author Arasple
 * @since 2022/10/29 10:59
 *
 * Panel 作为一个有长、宽的 UI 页面，
 * 可以作为 Window 的一个交互页面之一
 */
interface Panel : Parentable, Scalable {

    /**
     * 正在使用此 Panel 的 Windows
     */
    val windows: ArrayList<Window>

    /**
     * 该 Panel 的视野和定位
     * (长 x 宽 + 相对槽位 + 相对定位）
     */
    override val scale: ScaleView

    /**
     * 无序槽位
     *
     * @see ScaleView.slots
     */
    val slots: Set<Int>

    /**
     * Panel 的权重
     * 用于定义渲染、交互的优先级
     */
    var weight: PanelWeight

    /**
     * 注册
     */
    fun registerWindow(window: Window)

    /**
     * 注销
     */
    fun unregisterWindow(window: Window)

    /**
     * 遍历窗口
     */
    fun forWindows(function: Window.() -> Unit)

    /**
     * 向某个 Window 渲染此 Panel
     */
    fun renderPanel()

    /**
     * 检测某元素当前是否可渲染
     */
    fun getRenderability(element: Element): Set<Int>

    /**
     * 处理某元素的更新请求
     */
    fun renderElement(window: Window, element: Element)

    /**
     * 清除 Panel 指定槽位的已渲染物品
     */
    fun clearPanel(slots: Collection<Int>)

    fun clearPanel() = clearPanel(scale.slots)

    fun clearPanel(vararg slots: Int) = clearPanel(slots.toSet())

    /**
     * 处理 DragEvent
     */
    fun handleDrag(window: Window, e: InventoryDragEvent)

    /**
     * 处理 ClickEvent
     */
    fun handleClick(window: Window, e: InventoryClickEvent)

    /**
     * 处理 ItemsCollect
     */
    fun handleItemsCollect(window: Window, e: InventoryClickEvent)

    /**
     * 处理 ItemsMove
     */
    fun handleItemsMove(window: Window, e: InventoryClickEvent)

}