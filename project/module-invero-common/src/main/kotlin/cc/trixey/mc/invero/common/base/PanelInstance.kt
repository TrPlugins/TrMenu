package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Arasple
 * @since 2022/11/7 22:18
 * @see Panel
 *
 * 基础的 Panel 抽象实例
 */
abstract class PanelInstance(
    override val scale: PanelScale,
    override val pos: Int,
    weight: PanelWeight
) : Panel {

    /**
     * 父级
     */
    open var parent: PanelInstance? = null

    /**
     * 子集
     */
    open val children: ArrayList<PanelInstance>? = null

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
     * @see Panel.slots
     */
    override val slots by lazy { (0 until scale.area).toSet() }

    /**
     * @see Panel.slotsMap
     */
    override val slotsMap = ConcurrentHashMap<Int, MappedSlots>()

    /**
     * @see Panel.getSlotsMap
     */
    override fun getSlotsMap(parent: Parentable): MappedSlots {
        val width = when (parent) {
            is Window -> parent.type.width
            is PanelInstance -> parent.scale.width
            else -> error("?")
        }
        return slotsMap.computeIfAbsent(width) { MappedSlots.from(scale, pos, width) }
    }

    /**
     * @see Panel.getChildren
     */
    override fun getChildren() = children?.map { it }

    /**
     * @see Panel.getParent
     */
    override fun getParent() = parent as Parentable?

    /**
     * @see Panel.isRenderable
     */
    override fun isRenderable(element: Element): Boolean {
        return true
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
    override fun clearPanel(slots: Set<Int>) {
        forWindows {
            val windowSlotMap = getSlotsMap(this@forWindows)
            slots.forEach {
                inventorySet[windowSlotMap.getAbsolute(it)] = null
            }
        }
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

}