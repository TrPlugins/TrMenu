package me.arasple.mc.trmenu.modules.function.hook.impl

import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

/**
 * @author Arasple
 * @date 2020/8/27 20:24
 */
class HookPlayerPoints : HookInstance() {

    private lateinit var api: PlayerPointsAPI

    override fun getDepend(): String {
        return "PlayerPoints"
    }

    override fun initialization() {
        api = (Bukkit.getPluginManager().getPlugin(getDepend()) as PlayerPoints).api
    }

    fun addPoints(player: OfflinePlayer, amount: Int) {
        api.give(player.uniqueId, amount)
    }

    fun takePoints(player: OfflinePlayer, amount: Int) {
        api.take(player.uniqueId, amount)
    }

    fun setPoints(player: OfflinePlayer, amount: Int) {
        api.set(player.uniqueId, amount)
    }

    fun getPoints(player: OfflinePlayer): Int {
        return api.look(player.uniqueId)
    }

    fun hasPoints(player: OfflinePlayer, amount: Int): Boolean {
        return getPoints(player) >= amount
    }

}