package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.display.animation.AnimationHandler
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/5 13:48
 */
class ActionReset : Action("reset") {

    override fun onExecute(player: Player) {
        Menu.getMenus().forEach {
            it.icons.forEach { icon ->
                icon.defIcon.display.item.cache.remove(player.uniqueId)
                icon.subIcons.forEach { sub ->
                    sub.display.item.cache.remove(player.uniqueId)
                }
            }
        }
        AnimationHandler.reset(player)
    }

}