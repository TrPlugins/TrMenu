package me.arasple.mc.trmenu.api.event

import io.izzel.taboolib.module.event.EventCancellable
import me.arasple.mc.trmenu.modules.display.Menu
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/30 18:45
 */
class MenuSwitchPageEvent(val player: Player, val menu: Menu, val fromPage: Int, val toPage: Int) : EventCancellable<MenuSwitchPageEvent>()