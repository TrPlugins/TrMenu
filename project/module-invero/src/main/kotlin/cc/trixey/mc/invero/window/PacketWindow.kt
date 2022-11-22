package cc.trixey.mc.invero.window

import cc.trixey.mc.invero.common.InventorySet
import cc.trixey.mc.invero.common.Parentable
import cc.trixey.mc.invero.common.WindowType
import cc.trixey.mc.invero.common.base.BaseWindowPacket
import cc.trixey.mc.invero.common.event.InvEvent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/11/22 15:15
 */
open class PacketWindow(
    viewer: Player,
    override val type: WindowType,
    title: String,
    var lock: Boolean = true
) : BaseWindowPacket(viewer.uniqueId) {

    override var title: String
        get() = TODO("Not yet implemented")
        set(value) {}

    override val inventorySet: InventorySet
        get() = TODO("Not yet implemented")

    override fun open() {
        TODO("Not yet implemented")
    }

    override fun handleEvent(e: InvEvent) {
        TODO("Not yet implemented")
    }

    override fun isViewing(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getChildren(): List<Parentable>? {
        TODO("Not yet implemented")
    }

    override fun getParent(): Parentable? {
        TODO("Not yet implemented")
    }

}