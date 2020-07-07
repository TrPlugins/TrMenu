package me.arasple.mc.trmenu.api.events

import io.izzel.taboolib.module.event.EventCancellable
import me.arasple.mc.trmenu.display.Menu
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/17 16:29
 */
class MenuOpenEvent(val player: Player, val menu: Menu, val page: Int, val reason: Reason, var result: Result) : EventCancellable<MenuOpenEvent>() {

    fun isOpenedByBindings() = reason == Reason.BINDING_COMMANDS || reason == Reason.BINDING_ITEMS || reason == Reason.BINDING_SHORTCUT

    enum class Result {

        /**
         * Unknow page etc.
         */
        ERROR_PAGE,

        SUCCESSED,

        DENIED,

        UNKNOWN

    }

    enum class Reason {

        PLAYER_COMMAND,

        CONSOLE,

        OP,

        BINDING_COMMANDS,

        BINDING_ITEMS,

        BINDING_SHORTCUT

    }

}