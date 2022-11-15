package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.IOStoragePanel
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.ContainerWindow
import cc.trixey.mc.trmenu.invero.util.*
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XMaterial

/**
 * TrMenu
 * cc.trixey.mc.trmenu.test.unit.UnitIO
 *
 * @author Score2
 * @since 2022/11/15 21:11
 */
object UnitIO {

    val storage = subCommand {
        execute<Player> { player, _, _ ->
            buildWindow<ContainerWindow>(player) {
                title = "Storage Panels Test"

                val pane = randomPane()

                arrayOf(0, 45).forEach { pos ->
                    addPanel<StandardPanel>(9 to 1, pos) {
                        val paneItem = buildItem<BasicItem>(pane)

                        slotsUnoccupied.forEach { slot ->
                            setElement(slot, paneItem)
                        }
                    }
                }
                addPanel<IOStoragePanel>(9 to 4, 9) {
                    setElement(0..4, buildItem<BasicItem>(taboolib.platform.util.buildItem(generateRandomItem()){
                        name = "§bA random Item"
                        lore.add("§3You can move freely in this panel.")
                    }))

                }

            }.also { it.open() }

        }
    }

    fun randomPane(): Material {
        return Material.values().filter { it.name.endsWith("_stained_glass_pane", ignoreCase = true) }.random()
            ?: Material.WHITE_STAINED_GLASS_PANE
    }

/*
    @SubscribeEvent
    fun e(e: InventoryDragEvent) {
        println("================================> InventoryDragEvent")
        println("          type: " + e.type)
        println("        cursor: " + e.cursor.dump())
        println("     oldCursor: " + e.oldCursor.dump())
        println("      newItems: " + e.newItems.toList().map { "{${it.first}, ${it.second.dump()}}, " })
        println("      rawSlots: " + e.rawSlots)
        println("inventorySlots: " + e.inventorySlots)

    }

    @SubscribeEvent
    fun e(e: InventoryClickEvent) {
        println("================================> InventoryClickEvent")
        println("      action: " + e.action)
        println("       click: " + e.click)
        println(" currentItem: " + e.currentItem.dump())
        println("      cursor: " + e.cursor.dump())
        println("hotbarButton: " + e.hotbarButton)
        println("     rawSlot: " + e.rawSlot)
        println("        slot: " + e.slot)
        println("    slotType: " + e.slotType)
    }

    fun ItemStack?.dump() = "${this?.type}-${this?.amount}"
    */
}