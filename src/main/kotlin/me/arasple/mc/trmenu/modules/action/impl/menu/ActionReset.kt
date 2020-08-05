package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.display.animation.AnimationHandler
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/5 13:48
 */
class ActionReset : Action("reset") {

    override fun onExecute(player: Player) {
        AnimationHandler.reset(player)
    }

}