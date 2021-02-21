package me.arasple.mc.trmenu.api.receptacle.window

import me.arasple.mc.trmenu.api.event.ReceptacleInteractEvent
import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.api.receptacle.nms.NMS
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowClose
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowItems
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowOpen
import me.arasple.mc.trmenu.api.receptacle.nms.packet.PacketWindowSetSlot
import me.arasple.mc.trmenu.api.receptacle.window.handler.ClickHandler
import me.arasple.mc.trmenu.api.receptacle.window.handler.ReceptacleHandler
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Arasple
 * @date 2020/11/29 10:38
 */
open class Receptacle(
    private val inventoryType: InventoryType,
    var type: ReceptacleType,
    title: String = inventoryType.defaultTitle,
) {

    var title: String by Delegates.observable(title) { _, _, _ ->
        forViewers { initializationPackets(it) }
    }

    private var eventClick: ClickHandler = ClickHandler { _, _ -> }

    private var eventOpen: ReceptacleHandler = ReceptacleHandler { _, _ -> }

    private var eventClose: ReceptacleHandler = eventOpen

    private val viewers = mutableSetOf<UUID>()

    private val items: Array<ItemStack?> by lazy { arrayOfNulls(type.totalSize) }

    fun getItem(slot: Int) = items[slot]

    fun hasItem(slot: Int) = getItem(slot) != null

    fun setItem(itemStack: ItemStack? = null, slots: Collection<Int>, display: Boolean = true) {
        setItem(itemStack, *slots.toIntArray(), display = display)
    }

    fun setItem(itemStack: ItemStack? = null, vararg slots: Int, display: Boolean = true) {
        slots.forEach { items[it] = itemStack }
        val packets = slots.map { PacketWindowSetSlot(slot = it, itemStack = itemStack) }

        if (display) {
            forViewers { player ->
                packets.forEach { it.send(player) }
            }
        }
    }

    fun refresh(slot: Int) {
        if (slot < 0) return
        val packet = PacketWindowSetSlot(slot = slot, itemStack = items[slot])
        forViewers { player ->
            packet.send(player)
        }
    }

    private fun getViewers() = viewers.map { Bukkit.getPlayer(it) }

    fun listenerOpen(handler: ReceptacleHandler) {
        this.eventOpen = handler
    }

    fun listenerClose(handler: ReceptacleHandler) {
        this.eventClose = handler
    }

    fun listenerClick(clickHandler: ClickHandler) {
        this.eventClick = clickHandler
    }

    private fun forViewers(viewer: (Player) -> Unit) {
        getViewers().filterNotNull().forEach(viewer)
    }

    fun open(player: Player) {
        initializationPackets(player)
        ReceptacleAPI.MANAGER.getViewingReceptacle(player)?.viewers?.remove(player.uniqueId)
        ReceptacleAPI.MANAGER.setViewingReceptacle(player, this)
        viewers.add(player.uniqueId)

        eventOpen.run(player, this)
    }

    fun close(player: Player, closePacket: Boolean = true) {
        if (closePacket) PacketWindowClose().send(player)
        viewers.remove(player.uniqueId)
        eventClose.run(player, this)
        ReceptacleAPI.MANAGER.setViewingReceptacle(player, null)
    }

    fun callEventClick(player: Player, event: ReceptacleInteractEvent) {
        eventClick.run(player, event)
    }

    fun refresh(player: Player) {
        PacketWindowItems(items = items).send(player)
    }

    private fun initializationPackets(player: Player) {
        NMS.INSTANCE.sendInventoryPacket(
            player,
            PacketWindowOpen(type = type, title = title),
            PacketWindowItems(items = items)
        )
    }

    fun clearItems() {
        items.indices.forEach { items[it] = null }
    }

}
