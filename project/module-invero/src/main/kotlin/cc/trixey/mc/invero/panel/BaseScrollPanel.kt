package cc.trixey.mc.invero.panel

import cc.trixey.mc.invero.Element
import cc.trixey.mc.invero.PanelWeight
import cc.trixey.mc.invero.ScaleView
import cc.trixey.mc.invero.Window
import cc.trixey.mc.invero.item.ElemapSimplified
import cc.trixey.mc.invero.item.Interactable
import cc.trixey.mc.invero.panel.scroll.ScrollDirection
import cc.trixey.mc.invero.panel.scroll.ScrollType
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/11/17 11:19
 *
 * 滚动元素存储在 ScrollColum 中，通过单例 Elemap 的设置来实现滚动元素
 *
 * 考虑到 ScrollPanel 通过栏目来定位元素的特殊性，
 * 不支持自设默认元素（导航栏实现需要额外使用 StandardPanel）
 */
abstract class BaseScrollPanel(
    scale: ScaleView,
    weight: PanelWeight,
    open var direction: ScrollDirection,
    open var type: ScrollType
) : PanelInstance(scale, weight) {

    protected val elementsMap by lazy {
        ElemapSimplified(this)
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        super.handleClick(window, e)

        e.rawSlot.toLowerSlot()?.let {
            val element = elementsMap[it]
            if (element is Interactable) element.passClickEvent(e)
        }
    }

    override fun getRenderability(element: Element): Set<Int> {
        return elementsMap.findUpperSlots(this, element)
    }

    override fun renderPanel() {
        forWindows {
            elementsMap.forEach { renderElement(this, it) }
        }
    }

    fun getBase(): ElemapSimplified {
        return elementsMap
    }

    /**
     * 当前滚动定位
     */
    abstract var index: Int

    /**
     * 最大滚动定位
     */
    abstract val maxIndex: Int

    /**
     * 单个滚动栏目的容量
     */
    val columCapacity: Int by lazy {
        if (direction.isVertical) scale.width else scale.height
    }

    /**
     * 可视栏目总量
     *
     * 由 Panel 的尺寸和滚动方向决定
     */
    val columViewSize: Int by lazy {
        if (direction.isVertical) scale.height else scale.width
    }

    fun scroll(step: Int = 1) {
        index += step
    }

    fun next() = scroll(1)

    fun previous() = scroll(-1)

    fun getOccupiedSlots(): Set<Int> {
        return elementsMap.getOccupiedSlots()
    }

}
