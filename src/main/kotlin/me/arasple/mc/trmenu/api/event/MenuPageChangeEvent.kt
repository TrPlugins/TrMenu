package me.arasple.mc.trmenu.api.event

import io.izzel.taboolib.module.event.EventCancellable
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2021/2/4 11:33
 */
class MenuPageChangeEvent(val session: MenuSession, val frompage: Int, val toPage: Int, val override: Boolean) : EventCancellable<MenuPageChangeEvent>() {

    init {
        async(!Bukkit.isPrimaryThread())
    }

}