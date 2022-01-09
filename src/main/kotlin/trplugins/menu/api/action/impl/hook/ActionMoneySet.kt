package trplugins.menu.api.action.impl.hook

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.internal.hook.HookPlugin
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 16:21
 */
class ActionMoneySet(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val amount = parseContent(adaptPlayer(placeholderPlayer)).toDoubleOrNull() ?: -1.0
        if (amount > 0) {
            HookPlugin.getVault().setMoney(player, amount)
        }
    }

    companion object : ActionDesc {

        override val name = "(set|modify)-?(money|eco|coin)s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionMoneySet(value.toString(), option)
        }

    }

}