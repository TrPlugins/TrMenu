package me.arasple.mc.trmenu.modules.function.hook.impl

import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import me.realized.tokenmanager.api.TokenManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/27 20:28
 */
class HookTokenManager : HookInstance() {

    private lateinit var api: TokenManager

    override fun getDepend(): String {
        return "TokenManager"
    }

    override fun initialization() {
        api = Bukkit.getPluginManager().getPlugin(getDepend()) as TokenManager
    }

    fun addTokens(player: Player, amount: Long) {
        api.addTokens(player, amount)
    }

    fun takeTokens(player: Player, amount: Long) {
        api.removeTokens(player, amount)
    }

    fun setTokens(player: Player, amount: Long) {
        api.setTokens(player, amount)
    }

    fun getTokens(player: Player): Long {
        return api.getTokens(player).asLong
    }

    fun hasTokens(player: Player, amount: Long): Boolean {
        return getTokens(player) >= amount
    }

}