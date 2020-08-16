package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.module.compat.EconomyHook
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/16 20:48
 */
object HookEconomy {

    fun get(player: Player): Double {
        return if (HookCMI.isHooked()) HookCMI.getUser(player)?.economyAccount?.balance ?: -1.0 else EconomyHook.get(player)
    }

    fun add(player: Player, value: Double) {
        if (HookCMI.isHooked()) {
            HookCMI.getUser(player)?.economyAccount?.deposit(value)
        } else {
            EconomyHook.add(player, value)
        }
    }

    fun remove(player: Player, value: Double) {
        if (HookCMI.isHooked()) {
            HookCMI.getUser(player)?.economyAccount?.withdraw(value)
        } else {
            EconomyHook.remove(player, value)
        }
    }

    fun set(player: Player, value: Double) {
        if (HookCMI.isHooked()) {
            HookCMI.getUser(player)?.economyAccount?.setBalance(value)
        } else {
            EconomyHook.set(player, value)
        }
    }

}