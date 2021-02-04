package me.arasple.mc.trmenu.api.event

import io.izzel.taboolib.module.event.EventCancellable
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2021/1/29 17:34
 */
class MenuOpenEvent(val session: MenuSession, val page: Int, val reason: Reason = Reason.UNKNOWN) :
    EventCancellable<MenuOpenEvent>() {

    init {
        async(!Bukkit.isPrimaryThread())
    }

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