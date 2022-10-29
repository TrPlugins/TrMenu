package cc.trixey.mc.trmenu.legacy.invero.nms

import cc.trixey.mc.trmenu.legacy.invero.Invero
import cc.trixey.mc.trmenu.legacy.invero.InveroManager
import cc.trixey.mc.trmenu.legacy.invero.event.InveroCloseEvent
import cc.trixey.mc.trmenu.legacy.invero.event.InveroInteractEvent
import cc.trixey.mc.trmenu.legacy.invero.window.InteractAction
import cc.trixey.mc.trmenu.legacy.invero.window.InteractAction.*
import cc.trixey.mc.trmenu.legacy.invero.window.InteractType
import cc.trixey.mc.trmenu.legacy.invero.window.InteractType.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.PacketReceiveEvent
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import taboolib.platform.util.sendActionBar
import kotlin.math.min


/**
 * @author Arasple
 * @since 2022/10/20
 */
object WindowListener {

    private val indexs = if (isUniversal) {
        arrayOf("containerId", "slotNum", "buttonNum", "clickType", "carriedItem")
    } else {
        arrayOf("a", "slot", "button", "shift", "item")
    }

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        val player = e.player
        val packet = e.packet

        when (packet.name) {
            "PacketPlayInWindowClick" -> {
                val id = packet.read<Int>(indexs[0]) ?: return
                val invero = InveroManager.findViewingInvero(player, id) ?: return
                val slot = packet.read<Int>(indexs[1]) ?: return
                val button = packet.read<Int>(indexs[2]) ?: return
                val mode = Mode.valueOf(packet.read<Any>(indexs[3]).toString())
                val type = InteractType.find(mode, button, slot) ?: return
                val cursor = packet.read<Any?>(indexs[4])?.asCraftMirror()
                val action = identifyingAction(player, invero, type, cursor)
                if (type.isIgnoreSuggested) {
                    e.isCancelled = true
                    when (type) {
                        LEFT_MOUSE_DRAG_STARTING -> {

                        }
                        LEFT_MOUSE_DRAG_ADD_SLOT -> TODO()
                        LEFT_MOUSE_DRAG_ENDING -> TODO()
                        RIGHT_MOUSE_DRAG_STARTING -> TODO()
                        RIGHT_MOUSE_DRAG_ADD_SLOT -> TODO()
                        RIGHT_MOUSE_DRAG_ENDING -> TODO()
                        MIDDLE_MOUSE_DRAG_STARTING -> TODO()
                        MIDDLE_MOUSE_DRAG_ADD_SLOT -> TODO()
                        MIDDLE_MOUSE_DRAG_ENDING -> TODO()
                        else -> TODO()
                    }
                }

                invero.apply {
                    amend()
                    InveroInteractEvent(player, this, type, action, slot, cursor).apply {
                        call()
                        interactCallback.forEach { it(this) }
                        if (isCancelled) {
                            player.sendCancelCoursor()
                            refreshItem(slot)
                        }
                    }
                }

                e.isCancelled = true
                player.sendActionBar("ContainerID: $id")
            }

            "PacketPlayInCloseWindow" -> {
                val id = packet.read<Int>(if (isUniversal) "containerId" else "id") ?: return

                InveroManager.findViewingInvero(player, id)?.apply {
                    view.close()

                    InveroCloseEvent(player, this).apply {
                        call()
                        closeCallback(this)
                        if (shouldUpdate) {
                            player.updateInventory()
                        }
                    }
                    e.isCancelled = true
                }
            }
        }
    }


    /**
     * Identifying interact packet's action
     *
     * TODO check slot allowance (pickup/place)
     */
    private fun identifyingAction(
        player: Player, invero: Invero, type: InteractType, cursor: ItemStack?
    ): InteractAction {
        val mode = type.mode
        val button = type.button
        val slot = type.slot
        var action = UNKNOWN
        val clicked = invero.getItem(slot)
        val isCursorAir = cursor.isAir()
        val isClickedAir = clicked.isAir()
        val isLeftButton = button == 0
        val slotMaxStackSize = type.maxStackSize(slot)

        when (mode) {
            Mode.PICKUP -> {
                action = NOTHING

                if (slot == -999 && !isCursorAir) {
                    action = if (isLeftButton) DROP_ALL_CURSOR else DROP_ONE_CURSOR
                } else {
                    if (isClickedAir && !isCursorAir) {
                        action = if (isLeftButton) PLACE_ALL else PLACE_ONE
                    } else if (!isClickedAir) {
                        if (isCursorAir) {
                            action = if (isLeftButton) PICKUP_ALL else PICKUP_HALF
                        } else {
                            if (cursor!!.isSimilar(clicked)) {
                                val placeAmount = min(
                                    min(
                                        (if (isLeftButton) cursor.amount else 1),
                                        clicked!!.maxStackSize - clicked.amount
                                    ), slotMaxStackSize - clicked.amount
                                )
                                if (placeAmount == 1) {
                                    action = PLACE_ONE
                                } else if (placeAmount == cursor.amount) {
                                    action = PLACE_ALL
                                } else if (placeAmount < 0) {
                                    // this happens with oversized stacks
                                    action = if (placeAmount != -1) PICKUP_SOME else PICKUP_ONE
                                } else if (placeAmount != 0) {
                                    action = PLACE_SOME
                                }
                            } else if (cursor.amount <= slotMaxStackSize) {
                                action = SWAP_WITH_CURSOR
                            }
                        }
                    }
                }
            }

            Mode.QUICK_MOVE -> {
                action = if (slot <= 0) {
                    NOTHING
                } else {
                    if (!isClickedAir) MOVE_TO_OTHER_INVENTORY
                    else NOTHING
                }
            }

            Mode.SWAP -> {
                val hotbar = player.inventory.getItem(button)
                val canCleanSwap = hotbar.isAir() || slot in invero.property.slotsPlayerContents

                action = if (!isClickedAir) if (canCleanSwap) HOTBAR_SWAP
                else HOTBAR_MOVE_AND_READD
                else if (hotbar.isNotAir()) HOTBAR_SWAP
                else NOTHING
            }

            Mode.CLONE -> TODO()
            Mode.THROW -> TODO()
            Mode.QUICK_CRAFT -> {

            }

            Mode.PICKUP_ALL -> {
                if (!isCursorAir) {
                    if (invero.contents.filterNotNull().any { it.type == cursor?.type }) {
                        action = COLLECT_TO_CURSOR
                    }
                }
            }
        }

        return action
    }

}