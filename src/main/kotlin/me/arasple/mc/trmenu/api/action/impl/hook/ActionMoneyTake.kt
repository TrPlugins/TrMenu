package me.arasple.mc.trmenu.api.action.impl.hook

import io.izzel.taboolib.module.compat.EconomyHook
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 16:21
 */
class ActionMoneyTake(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val amount = parseContent(placeholderPlayer).toDoubleOrNull() ?: -1.0
        if (amount > 0) {
            EconomyHook.remove(player, amount)
        }
    }

    companion object {

        private val name = "(take|remove|withdraw)-?(money|eco|coin)s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionMoneyTake(value.toString(), option)
        }

        val registery = name to parser

    }

}