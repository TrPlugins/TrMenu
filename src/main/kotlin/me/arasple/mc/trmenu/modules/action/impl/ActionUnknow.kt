package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/1 9:39
 */
class ActionUnknow : Action("trmenu_action_unknow") {

    override fun onExecute(player: Player) = player.sendLocale("ERRORS.ACTION", getContent())

}