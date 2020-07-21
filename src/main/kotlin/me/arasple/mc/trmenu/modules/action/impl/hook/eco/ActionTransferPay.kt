package me.arasple.mc.trmenu.modules.action.impl.hook.eco

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.compat.EconomyHook
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/21 9:08
 */
class ActionTransferPay : Action("(pay|transfter)(-)?(money|eco|coin)?(s)?") {

    override fun onExecute(player: Player) {
        val args = getContentSplited(player, " ")
        if (args.size >= 2) {
            val receiver = Bukkit.getPlayerExact(args[0]) ?: return
            val money = NumberUtils.toDouble(args[1], -1.0)
            if (money > 0 && EconomyHook.get(player) >= money) {
                EconomyHook.remove(player, money)
                EconomyHook.add(receiver, money)
            }
        }
    }

}