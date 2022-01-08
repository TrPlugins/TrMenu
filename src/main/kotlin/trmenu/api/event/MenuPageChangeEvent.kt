package trmenu.api.event

import trmenu.module.display.MenuSession
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Arasple
 * @date 2021/2/4 11:33
 */
class MenuPageChangeEvent(val session: MenuSession, val from: Int, val to: Int, val override: Boolean) : BukkitProxyEvent()