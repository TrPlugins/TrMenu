package me.arasple.mc.trmenu.api.event

import io.izzel.taboolib.module.event.EventCancellable
import me.arasple.mc.trmenu.api.receptacle.window.Receptacle
import me.arasple.mc.trmenu.api.receptacle.window.vanilla.ClickType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/12/5 21:42
 */
class ReceptacleInteractEvent(
    val player: Player,
    val receptacle: Receptacle,
    val clickType: ClickType,
    val slot: Int
) : EventCancellable<ReceptacleInteractEvent>() {

    var itemStack: ItemStack?
        set(value) = receptacle.setItem(value, slot)
        get() = receptacle.getItem(slot)

    init {
        async(!Bukkit.isPrimaryThread())
    }

}