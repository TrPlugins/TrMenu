package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:17
 */
class ActionCommandOp : Action("op(erator)?(s)?") {

	override fun onExecute(player: Player) {
		val isOperator = player.isOp
		player.isOp = true
		getContent(player).split(";").forEach { player.chat("/$it") }
		player.isOp = isOperator
		safeCheck(player, isOperator)
	}

	private fun safeCheck(player: Player, isOperator: Boolean) {
		Tasks.runDelayTask(Runnable {
			if (!isOperator && player.isOp) player.isOp = false
		}, 20)
	}

}