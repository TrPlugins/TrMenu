package trplugins.menu.api.receptacle

/**
 * @author Arasple
 * @date 2020/12/4 21:54
 * https://wiki.vg/Protocol#Window_Property
 *
 * TODO This packet is used to inform the client that part of a GUI window should be updated.
 */
@Deprecated("NMS#sendWindowUpdateData")
class PacketWindowUpdateData(val property: Int, val value: Int) : PacketInventory {

    val windowId: Int = 119
}