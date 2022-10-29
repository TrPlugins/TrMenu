package cc.trixey.mc.trmenu.legacy.invero.event

import cc.trixey.mc.trmenu.legacy.invero.Invero
import cc.trixey.mc.trmenu.legacy.invero.window.InteractType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/10/21
 */
class InveroInteractEvent(
    player: Player,
    invero: Invero,
    val interactType: InteractType,
    val slot: Int,
    val carriedItem: ItemStack?
) : InveroEvent(player, invero) {

    var slotItem: ItemStack?
        get() = invero.getItem(slot)
        set(value) = invero.setItem(slot, value)

}