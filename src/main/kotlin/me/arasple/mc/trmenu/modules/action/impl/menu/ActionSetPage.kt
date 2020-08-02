package me.arasple.mc.trmenu.modules.action.impl.menu

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player
import kotlin.math.min

/**
 * @author Arasple
 * @date 2020/3/8 21:35
 */
class ActionSetPage : Action("((set|switch)?(-)?(shape|page))|shape|page") {

    override fun onExecute(player: Player) {
        val menu = getSession(player).menu ?: return
        val page = min(NumberUtils.toInt(getContent(player), 0), menu.layout.layouts.size - 1)
        menu.tasking.reset(player)
        menu.open(player, page, MenuOpenEvent.Reason.SWITCH_PAGE)
    }

}