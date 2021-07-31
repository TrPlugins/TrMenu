package me.arasple.mc.trmenu.api.event

import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyEvent

/**
 * @author Arasple
 * @date 2021/1/29 17:34
 */
class MenuOpenEvent(val session: MenuSession, val menu: Menu, val page: Int, val reason: Reason = Reason.UNKNOWN) :
    ProxyEvent() {

    override val allowAsynchronous get() = !Bukkit.isPrimaryThread()

    enum class Reason {

        RELOAD,

        PLAYER_COMMAND,

        CONSOLE,

        BINDING_COMMANDS,

        BINDING_ITEMS,

        BINDING_SHORTCUT,

        UNKNOWN

    }

}