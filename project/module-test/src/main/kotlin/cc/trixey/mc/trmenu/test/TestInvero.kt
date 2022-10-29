package cc.trixey.mc.trmenu.test

import org.bukkit.command.CommandSender
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.getProperty

/**
 * @author Arasple
 * @since 2022/10/29 10:51
 */
@CommandHeader(name = "testInvero")
object TestInvero {

    @CommandBody
    val unit1 = subCommand {
        execute<CommandSender> { _, _, _ ->

        }
    }

    @SubscribeEvent
    fun e(e: InventoryClickEvent) {
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


    @SubscribeEvent
    fun e(e: InventoryDragEvent) {
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

}