package me.arasple.mc.trmenu.modules.display.function

import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.modules.function.script.Scripts
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 13:34
 */
data class Reaction(val priority: Int, private val condition: String, private val success: List<Action>, private val failed: List<Action>, val hasCondition: Boolean = condition.isNotEmpty(), val isEmpty: Boolean = success.isEmpty() && failed.isEmpty()) {

    fun react(player: Player) = if (!isRequirementMatch(player)) {
        runDenyActions(player)
    } else {
        runActions(player)
    }

    fun isRequirementMatch(player: Player): Boolean = if (hasCondition) Scripts.expression(player, condition).asBoolean() else true

    fun runDenyActions(player: Player) = Actions.runActions(player, failed)

    fun runActions(player: Player) = Actions.runActions(player, success)

}