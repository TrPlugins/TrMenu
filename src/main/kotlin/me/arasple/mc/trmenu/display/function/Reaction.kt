package me.arasple.mc.trmenu.display.function

import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.script.Scripts
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 13:34
 */
data class Reaction(val priority: Int, private val condition: String, private val success: List<Action>, private val failed: List<Action>) {

    fun react(player: Player) = if (!isRequirementMatch(player)) {
        runDenyActions(player)
    } else {
        runActions(player)
    }

    fun isNotEmpty(): Boolean = condition.isNotEmpty()

    fun isRequirementMatch(player: Player): Boolean = if (isNotEmpty()) Scripts.expression(player, condition).asBoolean() else true

    fun runDenyActions(player: Player) = Actions.runActions(player, failed)

    fun runActions(player: Player) = Actions.runActions(player, success)


}