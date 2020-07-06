package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.script.Scripts
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/28 19:28
 */
class ActionJavaScript : Action("(java)?(-)?script(s)?|js") {

	override fun onExecute(player: Player) {
		Scripts.script(player, getContent())
	}

}