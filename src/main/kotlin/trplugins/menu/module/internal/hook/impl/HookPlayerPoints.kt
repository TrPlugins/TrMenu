package trplugins.menu.module.internal.hook.impl

import trplugins.menu.module.internal.hook.HookAbstract
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import taboolib.common.platform.function.submit
import java.util.concurrent.TimeUnit

/**
 * @author Arasple
 * @date 2021/1/26 22:18
 */
class HookPlayerPoints : HookAbstract() {

    private val playerPointsAPI: PlayerPointsAPI? = if (isHooked) (plugin as PlayerPoints).api else null
        get() {
            if (field == null) reportAbuse()
            return field
        }

    fun getPoints(player: OfflinePlayer): Int {
        return try {
            playerPointsAPI?.lookAsync(player.uniqueId)?.get(1, TimeUnit.SECONDS) ?: -1
        } catch (t: Throwable) {
            playerPointsAPI?.look(player.uniqueId) ?: -1
        }
    }

    fun setPoints(player: OfflinePlayer, amount: Int) {
        try {
            playerPointsAPI?.setAsync(player.uniqueId, amount)
        } catch (t: Throwable) {
            call { playerPointsAPI?.set(player.uniqueId, amount) }
        }
    }

    fun hasPoints(player: OfflinePlayer, amount: Int): Boolean {
        return getPoints(player) >= amount
    }

    fun addPoints(player: OfflinePlayer, amount: Int) {
        try {
            playerPointsAPI?.giveAsync(player.uniqueId, amount)
        } catch (t: Throwable) {
            call { playerPointsAPI?.give(player.uniqueId, amount) }
        }
    }

    fun takePoints(player: OfflinePlayer, amount: Int) {
        try {
            playerPointsAPI?.takeAsync(player.uniqueId, amount)
        } catch (t: Throwable) {
            call { playerPointsAPI?.take(player.uniqueId, amount) }
        }
    }

    private fun call(block: () -> Unit) {
        if (Bukkit.isPrimaryThread()) {
            block.invoke()
        } else {
            submit(async = false) { block.invoke() }
        }
    }

}