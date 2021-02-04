package me.arasple.mc.trmenu.api.event

import io.izzel.taboolib.module.event.EventNormal
import me.arasple.mc.trmenu.module.display.MenuSession

/**
 * @author Arasple
 * @date 2021/1/29 17:34
 */
class MenuCloseEvent(val session: MenuSession) : EventNormal<MenuCloseEvent>()