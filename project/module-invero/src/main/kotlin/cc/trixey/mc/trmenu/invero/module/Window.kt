package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryEvent

/**
 * @author Arasple
 * @since 2022/10/29 10:55
 *
 * Window 是提供交互窗口的容器
 * 其可以包含子对象 Panel，二者双向传递交互与渲染请求
 *
 * 一个 Window 仅允许包含一个 Viewer
 */
interface Window : Parentable {

    /**
     * 容器的标题
     */
    var title: String

    /**
     * 该 Window 包含的 Panel
     */
    val panels: ArrayList<Panel>

    /**
     * 该容器的类型
     */
    val type: TypeAddress

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
    fun handleEvent(e: InventoryEvent)

    /**
     * 返回当前观众是否正在查看本窗口
     */
    fun isViewing(): Boolean {
        return getViewerSafe()?.let {
            val holder = it.openInventory.topInventory.holder
            holder is WindowHolder && holder.window == this
        } ?: false
    }

}