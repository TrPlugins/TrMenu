package me.arasple.mc.trmenu.api.event

import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyEvent

/**
 * @author Arasple
 * @date 2021/2/4 11:33
 */
class MenuPageChangeEvent(val session: MenuSession, val from: Int, val to: Int, val override: Boolean) : ProxyEvent() {

    override val allowAsynchronous get() = !Bukkit.isPrimaryThread()

}