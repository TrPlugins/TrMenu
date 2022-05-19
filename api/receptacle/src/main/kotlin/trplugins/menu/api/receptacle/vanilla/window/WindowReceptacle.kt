package trplugins.menu.api.receptacle.vanilla.window

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submit
import taboolib.module.nms.nmsProxy
import trplugins.menu.api.receptacle.*

/**
 * @author Arasple
 * @date 2020/11/29 10:38
 */
open class WindowReceptacle(var type: WindowLayout, title: String = type.toBukkitType().defaultTitle) : Receptacle<ItemStack>(type) {

    private var viewer: Player? = null

    override var onOpen: ((player: Player, receptacle: Receptacle<ItemStack>) -> Unit) = { _, _ -> }

    override var onClose: ((player: Player, receptacle: Receptacle<ItemStack>) -> Unit) = { _, _ -> }

    override var onClick: ((player: Player, event: ReceptacleInteractEvent<ItemStack>) -> Unit) = { _, _ -> }

    private val contents by lazy { arrayOfNulls<ItemStack?>(type.totalSize) }

    private var hidePlayerInventory = false

    private var stateId = 1
        get() {
            return field++
        }

    override var title = title
        set(value) {
            field = value
            submit(delay = 3, async = true) {
                initializationPackets()
            }
        }

    fun hidePlayerInventory(hidePlayerInventory: Boolean) {
        this.hidePlayerInventory = hidePlayerInventory
    }

    override fun getElement(slot: Int): ItemStack? {
        setupPlayerInventorySlots()
        return contents.getOrNull(slot)
    }

    override fun hasElement(slot: Int): Boolean {
        return getElement(slot) != null
    }

    override fun setElement(element: ItemStack?, vararg slots: Int, display: Boolean) {
        slots.forEach { contents[it] = element }
        if (display && viewer != null) {
            slots.forEach { nmsProxy<NMS>().sendWindowsSetSlot(viewer!!, slot = it, itemStack = element, stateId = stateId) }
        }
    }

    override fun clear(display: Boolean) {
        contents.indices.forEach { contents[it] = null }
        if (display) {
            refresh()
        }
    }

    override fun refresh(slot: Int) {
        if (viewer != null) {
            setupPlayerInventorySlots()
            if (slot >= 0) {
                nmsProxy<NMS>().sendWindowsSetSlot(viewer!!, slot = slot, itemStack = contents[slot], stateId = stateId)
            } else {
                nmsProxy<NMS>().sendWindowsItems(viewer!!, items = contents)
            }
        }
    }

    override fun open(player: Player) {
        viewer = player
        initializationPackets()
        player.setViewingReceptacle(this)
        onOpen(player, this)
    }

    override fun close(sendPacket: Boolean) {
        if (viewer != null) {
            if (sendPacket) {
                nmsProxy<NMS>().sendWindowsClose(viewer!!)
            }
            onClose(viewer!!, this)
            viewer!!.setViewingReceptacle(null)
        }
    }

    override fun callEventClick(event: ReceptacleInteractEvent<ItemStack>) {
        if (viewer != null) {
            onClick(viewer!!, event)
        }
    }

    internal fun initializationPackets() {
        if (viewer != null) {
            nmsProxy<NMS>().sendWindowsOpen(viewer!!, title = title, type = type)
            refresh()
        }
    }

    internal fun setupPlayerInventorySlots() {
        if (hidePlayerInventory || viewer == null) {
            return
        }
        viewer!!.inventory.contents.forEachIndexed { index, itemStack ->
            if (itemStack != null) {
                val slot = when (index) {
                    in 0..8 -> type.hotBarSlots[index]
                    in 9..35 -> type.mainInvSlots[index - 9]
                    else -> -1
                }
                if (slot > 0) {
                    contents[slot] = itemStack
                }
            }
        }
    }
}
