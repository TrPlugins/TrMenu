package me.arasple.mc.trmenu.api.event

import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyEvent

/**
 * @author Arasple
 * @date 2021/1/29 17:34
 */
class MenuCloseEvent(val session: MenuSession) : ProxyEvent() {

    override val allowAsynchronous get() = !Bukkit.isPrimaryThread()

}