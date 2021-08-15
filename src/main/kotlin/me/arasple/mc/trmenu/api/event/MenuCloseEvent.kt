package me.arasple.mc.trmenu.api.event

import me.arasple.mc.trmenu.module.display.MenuSession
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Arasple
 * @date 2021/1/29 17:34
 */
class MenuCloseEvent(val session: MenuSession) : BukkitProxyEvent()