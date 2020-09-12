package me.arasple.mc.trmenu.api.action.impl.menu

import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.display.animation.AnimationHandler
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/5 13:48
 */
class ActionReset : Action("reset") {

    override fun onExecute(player: Player) {
        Menu.getMenus().forEach {
            it.icons.forEach { icon -> icon.resetCache(player.uniqueId) }
        }
        AnimationHandler.reset(player)
    }

}