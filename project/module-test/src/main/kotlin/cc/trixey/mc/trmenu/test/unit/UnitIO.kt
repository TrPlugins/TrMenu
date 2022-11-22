package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.WindowHolder
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.panel.IOStoragePanel
import cc.trixey.mc.invero.panel.PagedStandardPanel
import cc.trixey.mc.invero.panel.StandardPanel
import cc.trixey.mc.invero.util.*
import cc.trixey.mc.invero.window.ContainerWindow
import cc.trixey.mc.trmenu.test.generateRandomItem
import org.bukkit.Material
import taboolib.common.platform.command.subCommand

/**
 * TrMenu
 * cc.trixey.mc.trmenu.test.unit.UnitIO
 *
 * @author Score2
 * @since 2022/11/15 21:11
 */
object UnitIO {

    val storagePanel = buildPanel<IOStoragePanel>(3 to 3, 10, PanelWeight.SURFACE) {
        for (i in 0..8) {
            set(i, generateRandomItem {
                amount = (1..10).random()

                name = "§bA random Item"
                lore.add("§3You can move freely in this panel.")
            })
        }

        onClick {
            println("================================> InventoryClickEvent")
            println("      action: " + it.action)
            println("       click: " + it.click)
            println(" currentItem: " + it.currentItem)
            println("      cursor: " + it.cursor)
            println("hotbarButton: " + it.hotbarButton)
            println("     rawSlot: " + it.rawSlot)
            println("        slot: " + it.slot)
            println("    slotType: " + it.slotType)
        }

        onMove {
            it.clickedInventory?.apply {
                if (holder is WindowHolder) {
                    println("================================> WindowMoveItemsEvent")
                    println("     current: " + it.currentItem)
                    println("     cursor: " + it.cursor)
                    println("     slotType: " + it.slotType)
                    println("     slot: " + it.slot)
                    println("     action: " + it.action)
                    println("     rawSlot: " + it.rawSlot)
                }
            }
        }

        onDrag { it ->
            println("================================> InventoryDragEvent")
            println("          type: " + it.type)
            println("        cursor: " + it.cursor)
            println("     oldCursor: " + it.oldCursor)
            println("      newItems: " + it.newItems.toList().map { "{${it.first}, ${it.second}}, " })
            println("      rawSlots: " + it.rawSlots)
            println("inventorySlots: " + it.inventorySlots)
        }
    }

    val command = subCommand {
        execute { player, _, _ ->
            buildWindow<ContainerWindow>(player) {
                lock = false
                title = "Storage Panels Test"

                this += storagePanel

                addPanel<StandardPanel>(5 to 5, 0, PanelWeight.BACKGROUND) {
                    buildItem<BasicItem>(Material.GRAY_STAINED_GLASS_PANE).set(getUnoccupiedSlots())
                }

                addPanel<PagedStandardPanel>(3 to 6, 6) {

                    background {
                        buildItem<BasicItem>(Material.ARROW, {
                            name = "§3Previous page"
                            lore.add("$pageIndex / $maxPageIndex")
                        }, {
                            onClick {
                                previousPage()
                                modifyLore { set(0, "$pageIndex / $maxPageIndex") }
                                forWindows { title = "Page: $pageIndex / $maxPageIndex" }
                            }
                        }).set(0)

                        buildItem<BasicItem>(Material.ARROW, {
                            name = "§aNext Page"
                            lore.add("$pageIndex / $maxPageIndex")
                        }, {
                            onClick {
                                nextPage()
                                modifyLore { set(0, "$pageIndex / $maxPageIndex") }
                                forWindows { title = "Page: $pageIndex / $maxPageIndex" }
                            }
                        }).set(2)
                    }

                    val fill: () -> BasicItem = { buildItem(generateRandomItem()) }

                    page {
                        add(getUnoccupiedSlots(it), buildItem<BasicItem>(Material.BLACK_STAINED_GLASS))
                    }
                    page {
                        add(getUnoccupiedSlots(it), buildItem<BasicItem>(Material.LIME_STAINED_GLASS_PANE))
                    }

                    for (i in 0..10) page { add(getUnoccupiedSlots(it), fill()) }
                }

            }.also { it.open() }
        }
    }

}