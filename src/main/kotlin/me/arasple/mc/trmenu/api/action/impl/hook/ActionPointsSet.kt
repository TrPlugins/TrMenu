package me.arasple.mc.trmenu.api.action.impl.hook

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 16:21
 */
class ActionPointsSet(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
            val amount = parseContent(placeholderPlayer).toDoubleOrNull() ?: -1.0
            if (amount > 0) {
                HookPlugin.getOptEco().setPoints(player, amount)
                HookPlugin.getPlayerPoints().setPoints(player, amount.toInt)
            }
    }

    companion object {

        private val name = "(set|modify)-?points?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionPointsSet(value.toString(), option)
        }

        val registery = name to parser

    }

}