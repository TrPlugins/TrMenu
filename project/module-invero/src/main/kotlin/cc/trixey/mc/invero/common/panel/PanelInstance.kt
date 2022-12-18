package cc.trixey.mc.invero.common.panel

import cc.trixey.mc.invero.common.*
import cc.trixey.mc.invero.util.distinguishMark
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * @author Arasple
 * @since 2022/11/7 22:18
 * @see Panel
 *
 * 基础的 Panel 抽象实例
 */
abstract class PanelInstance(scale: ScaleView, weight: PanelWeight) : Panel {

    /**
     * 父级
     */
    private var panelParent: Parentable? = null

    /**
     * 无序槽位
     *
     * @see ScaleView.slots
     */
    override val slots: Set<Int> by lazy { scale.slots.toSet() }

    /**
     * 槽位映射和大小、分布
     */
    override val scale: ScaleView = scale
        get() {
            initScale(field)
            return field
        }

    /**
     * @see Panel.windows
     */
    override val windows: ArrayList<Window> = ArrayList()
        get() {
            getParent()?.let {
                if (it is PanelInstance) return it.windows
            }
            return field
        }

    /**
     * @see Panel.weight
     */
    override var weight = weight
        set(value) {
            field = value
            forWindows { renderWindow(true) }
        }

    /**
     * @see Panel.getChildren
     */
    override fun getChildren(): ArrayList<Panel>? = null

    /**
     * @see Panel.getParent
     */
    override fun getParent() = panelParent

    /**
     * @see Panel.setParent
     */
    override fun setParent(parentable: Parentable) {
        panelParent = parentable
        scale.resetCache()
    }

    /**
     * @see Panel.unregisterWindow
     */
    override fun unregisterWindow(window: Window) {
        windows -= window
    }

    /**
     * @see Panel.registerWindow
     */
    override fun registerWindow(window: Window) {
        windows += window
    }

    /**
     * @see Panel.forWindows
     */
    override fun forWindows(function: Window.() -> Unit) {
        windows.forEach(function)
    }

    /**
     * @see Panel.clearPanel
     */
    override fun clearPanel(slots: Collection<Int>) {
        forWindows {
            slots.mapNotNull { it.toUpperSlot() }.forEach {
                inventorySet[it] = null
            }
        }
    }

    /**
     * @see Panel.renderElement
     */
    override fun renderElement(window: Window, element: Element) {
        if (element !is ItemProvider) return
        val itemStack = element.get()

        getRenderability(element).forEach { window.inventorySet[it] = itemStack.distinguishMark(it) }
    }

    /*
    相关容器事件的传递处理
     */

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    override fun handleDrag(window: Window, e: InventoryDragEvent) {
        e.isCancelled = true
    }

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        e.isCancelled = true
    }

    /**
     * 工具
     */

    private fun initScale(scale: ScaleView) {
        if (scale.getCache() == null) {
            val parent = panelParent ?: windows.firstOrNull()
            if (parent != null) scale.initCache((parent as Scalable))
        }
    }

    open fun Int.toUpperSlot(): Int? {
        scale.getCache()!![this]?.let {
            if (it in (getParent() as Scalable).scale.slots) return it
        }
        return null
    }

    open fun Int.toLowerSlot(): Int? {
        scale.getCacheReversed()!![this]?.let {
            if (it in (getParent() as Scalable).scale.slots) return it
        }
        return null
    }

    open fun getUpperSlots(): Set<Int> {
        return scale.getCache()!!.values.toSet()
    }

}