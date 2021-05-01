package me.arasple.mc.trmenu.module.internal.hook.impl

import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import me.playernguyen.opteco.api.OptEcoAPI
import org.bukkit.OfflinePlayer

class HookOptEco : HookAbstract() {

    private val OptEcoAPI: OptEcoAPI? = if (isHooked) OptEcoAPI() else null
        get() {
            if (field == null) reportAbuse()
            return field
        }

    fun getPoints(player: OfflinePlayer): Double {
        return OptEcoAPI?.getPoints(player.uniqueId) ?: -1.0
    }

    fun setPoints(player: OfflinePlayer, amount: Double) {
        OptEcoAPI?.setPoints(player.uniqueId, amount)
    }

    fun hasPoints(player: OfflinePlayer, amount: Double): Boolean {
        return getPoints(player) >= amount
    }

    fun addPoints(player: OfflinePlayer, amount: Double) {
        OptEcoAPI?.addPoints(player.uniqueId, amount)
    }

    fun takePoints(player: OfflinePlayer, amount: Double) {
        OptEcoAPI?.takePoints(player.uniqueId, amount)
    }

}
