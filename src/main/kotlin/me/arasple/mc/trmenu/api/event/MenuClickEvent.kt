package me.arasple.mc.trmenu.api.event

import io.izzel.taboolib.module.event.EventCancellable
import me.arasple.mc.trmenu.api.inventory.InvClickType
import me.arasple.mc.trmenu.modules.display.Icon
import me.arasple.mc.trmenu.modules.display.Menu
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/6 21:44
 */
class MenuClickEvent(val player: Player, val slot: Int, val menu: Menu, val icon: Icon?, val clickType: InvClickType) : EventCancellable<MenuClickEvent>()