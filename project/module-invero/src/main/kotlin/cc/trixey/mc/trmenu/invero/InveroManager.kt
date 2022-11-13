package cc.trixey.mc.trmenu.invero

import cc.trixey.mc.trmenu.invero.impl.WindowHolder
import cc.trixey.mc.trmenu.invero.impl.element.BasicDynamicItem
import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.PagedGeneratorPanel
import cc.trixey.mc.trmenu.invero.impl.panel.PagedNetesedPanel
import cc.trixey.mc.trmenu.invero.impl.panel.PagedStandardPanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.CompleteWindow
import cc.trixey.mc.trmenu.invero.impl.window.ContainerWindow
import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.element.PanelElement
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import org.bukkit.entity.Player
import org.bukkit.event.inventory.*
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @since 2022/10/28 19:22
 *
 * TODO List
 *
 * - #[?] ItemElement 请求更新逻辑
 * - #[?] 周期性帧 ItemElement
 *
 * - #[√] DynamicElement 动态槽位
 * ^ 至此已实现支持 TrMenu v3 的功能框架
 *
 * -
 */
object InveroManager {

    fun Panel.constructElement(type: Class<*>): PanelElement {
        return when (type) {
            BasicItem::class.java -> BasicItem(null, this)
            BasicDynamicItem::class.java -> BasicDynamicItem(null, this)
            else -> throw IllegalArgumentException("Failed to create element, not found registered class $type")
        }
    }

    fun constructPanel(type: Class<*>, scale: Pair<Int, Int>, posMark: Int, weight: PanelWeight): Panel {
        return when (type) {
            StandardPanel::class.java -> StandardPanel(scale, posMark, weight)
            PagedStandardPanel::class.java -> PagedStandardPanel(scale, posMark, weight)
            PagedNetesedPanel::class.java -> PagedNetesedPanel(scale, posMark, weight)
            PagedGeneratorPanel::class.java -> PagedGeneratorPanel(scale, posMark, weight)
            else -> throw IllegalArgumentException("Failed to create panel, not found registered class $type")
        }
    }

    fun constructWindow(type: Class<*>, viewer: Player, windowType: TypeAddress, title: String): Window {
        return when (type) {
            ContainerWindow::class.java -> ContainerWindow(viewer, windowType, title)
            CompleteWindow::class.java -> CompleteWindow(viewer, windowType, title)
            else -> throw IllegalArgumentException("Failed to create window, not found registered class $type")
        }
    }

    @SubscribeEvent
    fun e(e: InventoryClickEvent) = e.passInventoryEvent()

    @SubscribeEvent
    fun e(e: InventoryDragEvent) = e.passInventoryEvent()

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) = e.passInventoryEvent()

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) = e.passInventoryEvent()

    private fun InventoryEvent.passInventoryEvent() = inventory.holder.let {
        if (it is WindowHolder) {
            it.window.handleEvent(this)
        }
    }

}