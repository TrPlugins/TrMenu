package trplugins.menu.api.action.impl.hook

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.internal.hook.HookPlugin

/**
 * TrMenu
 * trplugins.menu.api.action.impl.hook.SetMoney
 *
 * @author Score2
 * @since 2022/02/14 14:17
 */
class SetMoney(handle: ActionHandle) : ActionBase(handle) {
    override val regex = "(set|modify)-?(money|eco|coin)s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val amount = contents.stringContent().parseContent(placeholderPlayer).toDoubleOrNull() ?: -1.0
        if (amount > 0) {
            HookPlugin.getVault().setMoney(player.cast(), amount)
        }
    }
}