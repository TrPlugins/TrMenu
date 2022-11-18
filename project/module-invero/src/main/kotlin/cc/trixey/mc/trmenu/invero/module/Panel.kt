package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import java.util.concurrent.ConcurrentMap

/**
 * @author Arasple
 * @since 2022/10/29 10:59
 *
 * Panel 作为一个有长、宽的 UI 页面，
 * 可以作为 Window 的一个交互页面之一
 */
interface Panel : Parentable {

    /**
     * 正在使用此 Panel 的 Windows
     */
    val windows: ArrayList<Window>

    /**
     * 该 Panel 的规模
     * 长 x 宽
     */
    val scale: Pair<Int, Int>

    /**
     * 该 Panel 的有效相对槽位
     */
    val slots: Set<Int>

    /**
     * 该 Panel 的左上角的定位点
     */
    val pos: Int

    /**
     * Panel 的权重
     * 用于定义渲染、交互的优先级
     */
    var weight: PanelWeight

    /**
     * 针对不同类型的 Window/父级 Panel 的映射槽位集
     */
    val slotsMap: ConcurrentMap<Int, MappedSlots>

    /**
     * 取得针对父级对象的映射槽位集
     */
    fun getSlotsMap(parent: Parentable): MappedSlots

    /**
     * 检测某元素当前是否可渲染
     */
    fun isRenderable(element: PanelElement): Boolean

    /**
     * 注销
     */
    fun unregisterWindow(window: Window)

    /**
     * 注册
     */
    fun registerWindow(window: Window)

    /**
     * 遍历窗口
     */
    fun forWindows(function: Window.() -> Unit)

    /**
     * 渲染全部窗口
     */
    fun renderAll()

    /**
     * 向某个 Window 渲染此 Panel
     */
    fun renderPanel(window: Window)

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