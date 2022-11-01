package cc.trixey.mc.trmenu.invero.impl

import cc.trixey.mc.trmenu.invero.module.BaseWindow
import cc.trixey.mc.trmenu.invero.module.PairedInventory
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.util.handler
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submit
import taboolib.library.reflex.Reflex.Companion.getProperty

/**
 * @author Arasple
 * @since 2022/10/29 16:23
 *
 * 一个标准的容器窗口
 * 玩家的背包将不受到调用和影响
 */
open class ContainerWindow(
    viewer: Player, title: String = "Untitled", override val type: TypeAddress = TypeAddress.ofRows(6)
) : BaseWindow(viewer.uniqueId) {

    override var title = title
        set(value) {
            getViewerSafe()?.let {
                handler.updateWindowTitle(it, this, value)
                // 防止未处理的残留虚拟容器
                submit {
                    if (!hasViewer()) {
                        handler.closeWindow(it)
                    }
                }
            }
            field = value
        }

    override val pairedInventory by lazy {
        PairedInventory(this, null)
    }

    override fun open() {
        getViewer().openInventory(pairedInventory.container)
    }

    override fun handleClick(e: InventoryClickEvent) {
        e.whoClicked.sendMessage(
            """
                §8——————————————————————————————§f
                Action: ${e.action.name}
                ClickType: ${e.click.name}
                SlotType: ${e.slotType.name}
                Slot: ${e.slot}
                HotbarButton: ${e.hotbarButton}
                Current: ${e.currentItem}
                
                §3>> Reflection:
                §2whichSlot: ${e.getProperty<Int>("whichSlot")}
                §2rawSlot: ${e.getProperty<Int>("rawSlot")}
                §2hotbarKey: ${e.getProperty<Int>("hotbarKey")}
                §2current: ${e.getProperty<ItemStack>("current")}
            """.trimIndent()
        )
    }

    override fun handleDrag(e: InventoryDragEvent) {
        e.whoClicked.sendMessage(
            """
                §3——————————————————————————————§f
                DragType: ${e.type.name}
                NewItems: ${e.newItems}
                
                Cursor: ${e.cursor}
                OldCursor: ${e.oldCursor}
                
                RawSlots: ${e.rawSlots}
                InventorySlots: ${e.inventorySlots}
            """.trimIndent()
        )
    }

    /*
    ItemsMove (Shift 移动物品) 和 ItemsCollect (双击收集物品)
    都应当考虑兼容 IO Panel 物品输入和输出
     */

    override fun handleItemsMove(e: InventoryClickEvent) {
        e.whoClicked.sendMessage("\nItemsMoveHandle\n")
    }

    override fun handleItemsCollect(e: InventoryClickEvent) {
        e.whoClicked.sendMessage("\nhandleItemsCollect\n")
    }

    override fun handleOpen(e: InventoryOpenEvent) {
        e.player.sendMessage("\nhandleOpen\n")
    }

    override fun handleClose(e: InventoryCloseEvent) {
        e.player.sendMessage("\nhandleClose\n")
    }

}