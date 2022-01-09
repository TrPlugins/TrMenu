package trplugins.menu.module.internal.hook.impl

import trplugins.menu.module.internal.hook.HookAbstract
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer


/**
 * @author Arasple
 * @date 2021/2/9 11:45
 */
class HookVault : HookAbstract() {

    private val economyAPI: Economy? = if (isHooked) {
        Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)?.provider
    } else null
        get() {
            if (field == null) reportAbuse()
            return field
        }


    fun takeMoney(player: OfflinePlayer, money: Double) {
        economyAPI?.withdrawPlayer(player, money)
    }

    fun addMoney(player: OfflinePlayer, money: Double) {
        economyAPI?.depositPlayer(player, money)
    }

    fun hasMoney(player: OfflinePlayer, money: Double): Boolean {
        return economyAPI?.has(player, money) ?: false
    }

    fun getMoney(player: OfflinePlayer): Double {
        return economyAPI?.getBalance(player) ?: 0.0
    }

    fun setMoney(player: OfflinePlayer, money: Double) {
        addMoney(player, money - getMoney(player))
    }

}