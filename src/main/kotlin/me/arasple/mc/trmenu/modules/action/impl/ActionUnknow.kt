package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/1 9:39
 */
class ActionUnknow : Action("trmenu_action_unknow") {

	override fun onExecute(player: Player) = TLocale.sendTo(player, "ERRORS.ACTION", getContent())

}