package me.arasple.mc.trmenu.display.function

import me.arasple.mc.trmenu.modules.action.ActionHandler
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.script.Scripts
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 13:34
 */
class Requirement(private val condition: String, private val success: List<Action>, private val failed: List<Action>) {

    fun eval(player: Player): Boolean = if (!isRequirementMatch(player)) {
        runDenyActions(player)
        false
    } else {
        runActions(player)
        true
    }

    private fun isNotEmpty(): Boolean = condition.isNotEmpty()

    private fun isRequirementMatch(player: Player): Boolean = if (isNotEmpty()) Scripts.expression(player, condition).asBoolean() else true

    private fun runDenyActions(player: Player) = ActionHandler.runActions(player, failed)

    private fun runActions(player: Player) = ActionHandler.runActions(player, success)


}