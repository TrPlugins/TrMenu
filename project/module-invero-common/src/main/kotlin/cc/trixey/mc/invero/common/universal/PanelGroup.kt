package cc.trixey.mc.invero.common.universal

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.PanelScale
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.Window
import cc.trixey.mc.invero.common.base.PanelInstance
import cc.trixey.mc.invero.common.util.findPanel
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * @author Arasple
 * @since 2022/11/22 18:40
 *
 * Paenl 集
 * 子 Paenl 的应用范围不得超过父级 （PanelGroup）的 scale/pos
 */
class PanelGroup(
    scale: PanelScale,
    pos: Int,
    weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    internal val panels = mutableSetOf<PanelInstance>()

    override fun renderPanel() {
        panels.sortedByDescending { it.weight }.forEach { it.renderPanel() }
    }

    override fun renderElement(window: Window, element: Element) {
        error("This panel do not support element")
    }

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        findPanel(e.rawSlot)?.handleClick(window, e)
    }

    override fun handleDrag(window: Window, e: InventoryDragEvent) {
        findPanel(e.rawSlots.random())?.handleDrag(window, e)
    }

    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        findPanel(e.rawSlot)?.handleItemsCollect(window, e)
    }

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        findPanel(e.rawSlot)?.handleItemsMove(window, e)
    }

    /**
     * 拓展函数
     */

    fun <T : PanelInstance> T.grouped(): T {
        panels.add(this.also { it.setParent(this@PanelGroup) })
        return this
    }

}