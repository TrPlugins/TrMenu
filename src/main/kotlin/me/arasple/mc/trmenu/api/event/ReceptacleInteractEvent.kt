package me.arasple.mc.trmenu.api.event

import taboolib.module.ui.receptacle.Receptacle
import taboolib.module.ui.receptacle.ReceptacleClickType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyEvent

/**
 * @author Arasple
 * @date 2020/12/5 21:42
 */
class ReceptacleInteractEvent(
    val player: Player,
    val receptacle: Receptacle,
    val clickType: ReceptacleClickType,
    val slot: Int
) : ProxyEvent() {

    var itemStack: ItemStack?
        set(value) = receptacle.setItem(value, slot)
        get() = receptacle.getItem(slot)

    override val allowAsynchronous get() = !Bukkit.isPrimaryThread()

}