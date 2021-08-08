package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/1 12:31
 */
class ActionKether(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        TrMenuAPI.instantKether(player, baseContent, 1629043800L)
    }

    companion object {

        private val name = "ke(ther)?s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { it, option ->
            ActionKether(it.toString(), option)
        }

        val registery = name to parser

    }

}
